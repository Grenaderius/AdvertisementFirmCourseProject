package com.example.advertisementfirmcourseproject.UserControllers;

import ImageChangers.ImageSizeChanger;
import Paths.Paths;
import SQL.InteractionsInterface.DesignerInteractions.SQLDesignerInteractions;
import SQL.PostgreSQLConnection;
import com.example.advertisementfirmcourseproject.AdvertisementFirm;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class DesignerController {
    private final Paths paths = new Paths();
    private final ImageSizeChanger imageSizeChanger = new ImageSizeChanger();
    private String imagePathFromImageView;
    private final PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
    private String initials;

    @FXML
    Label helloLabel, surelySendLabel, contractNumbLabel;

    @FXML
    ImageView imageViewForDesigns;

    @FXML
    Button sendDesignBtn, exitBtn, yesBtn, noBtn, viewDesignsBtn;

    @FXML
    TextField contractNumTF;

    @FXML
    AnchorPane viewDesignsAnchorPane;

    public void setInitials(String initials){
        helloLabel.setText("Вітаємо в акаунті дизайнера, " + initials + "!");
        this.initials = initials;
    }

    public void setImage(BufferedImage image) {
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        imageViewForDesigns.setImage(fxImage);
    }

    @FXML
    private void sendDesign(){
        //даємо можливість користувачу завантажити файл
        FileChooser fileChooser = new FileChooser();
        surelySendLabel.setVisible(false);
        surelySendLabel.setText("Ви точно хочете передати цей дизайн?");
        surelySendLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Зображення (jpg, png)", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        Stage stage = (Stage) imageViewForDesigns.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        try {
            imagePathFromImageView = file.getAbsolutePath();
            yesBtn.setVisible(true);
            noBtn.setVisible(true);
            contractNumTF.setVisible(true);
            contractNumbLabel.setVisible(true);
            imageViewForDesigns.setVisible(true);

            try {
                setImage(imageSizeChanger.changeSize(file, 400, 400));
            } catch (Exception e) {
                surelySendLabel.setText("Помилка! Не вдалося завантажити зображення!!!");
                surelySendLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }

        } catch (Exception e) {
            surelySendLabel.setText("Вибрано не правильний тип зображення!!!");
            surelySendLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        }



        surelySendLabel.setVisible(true);
        viewDesignsAnchorPane.setVisible(false);
    }

    @FXML
    private void yesSendBtn() throws IOException {
        surelySendLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");

        if(contractNumTF.getText().isEmpty()){
            surelySendLabel.setText("Уведіть номер договору!!!");
        }else {
            File file = new File(imagePathFromImageView);
            String imgName = file.getName();

            Connection connection = postgreSQLConnection.connect();
            Statement stmt;
            ResultSet rs;

            //отримуємо ід дизайнера
            try {
                stmt = connection.createStatement();

                String sql = "Select designer_id from designers where designer_initials='" + initials + "' ;";
                rs = stmt.executeQuery(sql);
                int initialsId = 0;

                while(rs.next()) {
                    initialsId = Integer.parseInt(rs.getString("designer_id"));
                }

                //додаємо інформацію до бд
                sql = "INSERT INTO designs (design_name, designer_id, contracts_id) VALUES (?, ?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, imgName);
                preparedStatement.setInt(2, initialsId);
                preparedStatement.setInt(3, Integer.parseInt(contractNumTF.getText()));

                preparedStatement.executeUpdate();

                surelySendLabel.setText("Готово!!!");
                connection.close();

                Path pathToImg = file.toPath();
                Path pathToSave = new File(paths.getImagesPath() + imgName).toPath();
                Files.copy(pathToImg, pathToSave, StandardCopyOption.REPLACE_EXISTING);
            } catch (SQLException e) {
                surelySendLabel.setVisible(true);
                surelySendLabel.setText("Сталася помилка при спробі зберегти зображення!!!");
                surelySendLabel.setStyle("-fx-text-fill: RED; -fx-font-size: 14px;");
            }

            contractNumTF.setVisible(false);
            yesBtn.setVisible(false);
            noBtn.setVisible(false);
            contractNumbLabel.setVisible(false);

        }
    }

    @FXML
    private void noSendBtn() throws IOException {
        surelySendLabel.setVisible(false);
        yesBtn.setVisible(false);
        noBtn.setVisible(false);
        contractNumTF.setVisible(false);
        contractNumbLabel.setVisible(false);

        File file = new File(paths.getImagesPath() + "baseImage.png");
        setImage(imageSizeChanger.changeSize(file, 400, 400));
    }

    @FXML
    private void viewDesigns() throws SQLException, IOException {
        SQLDesignerInteractions sqlDesignerInteractions = new SQLDesignerInteractions(viewDesignsAnchorPane);
        sqlDesignerInteractions.createView();

        surelySendLabel.setVisible(false);
        yesBtn.setVisible(false);
        noBtn.setVisible(false);
        contractNumTF.setVisible(false);
        contractNumbLabel.setVisible(false);
        imageViewForDesigns.setVisible(false);

        viewDesignsAnchorPane.setVisible(true);
    }

    @FXML
    private void exit() throws IOException {
        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();

        AdvertisementFirm advertisementFirm = new AdvertisementFirm();
        advertisementFirm.start(stage);
    }
}
