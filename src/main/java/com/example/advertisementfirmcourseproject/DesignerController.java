package com.example.advertisementfirmcourseproject;

import ImageChangers.ImageSizeChanger;
import Paths.Paths;
import SQL.PostgreSQLConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    @FXML
    VBox vbox;

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

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Зображення (jpg, png)", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        Stage stage = (Stage) imageViewForDesigns.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        imagePathFromImageView = file.getAbsolutePath();

            try {
                setImage(imageSizeChanger.changeSize(file, 400, 400));
            } catch (Exception e) {
                surelySendLabel.setVisible(true);
                surelySendLabel.setText("Помилка! Не вдалося завантажити зображення!!!");
            }

        surelySendLabel.setVisible(true);
        yesBtn.setVisible(true);
        noBtn.setVisible(true);
        contractNumTF.setVisible(true);
        contractNumbLabel.setVisible(true);
        imageViewForDesigns.setVisible(true);

        viewDesignsAnchorPane.setVisible(false);
    }

    @FXML
    private void yesSendBtn() throws IOException, SQLException {
        if(contractNumTF.getText().isEmpty()){
            surelySendLabel.setText("Уведіть номер договору!!!");
        }else {
            File file = new File(imagePathFromImageView);
            String imgName = file.getName();

            Path pathToImg = file.toPath();
            Path pathToSave = new File(paths.getImagesPath() + imgName).toPath();
            Files.copy(pathToImg, pathToSave, StandardCopyOption.REPLACE_EXISTING);

            Connection connection = postgreSQLConnection.connect();
            Statement stmt;
            ResultSet rs;

            //отримуємо ід дизайнера
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
            yesBtn.setVisible(false);
            noBtn.setVisible(false);
            contractNumbLabel.setVisible(false);
            contractNumTF.setVisible(false);
            connection.close();
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
        createDesignsView();
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

        HelloApplication helloApplication = new HelloApplication();
        helloApplication.start(stage);
    }

    private void createDesignsView() throws SQLException, IOException {
        vbox = new VBox(10);
        vbox.setPrefWidth(388);
        vbox.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");

        Connection connection = postgreSQLConnection.connect();
        Statement stmt;
        ResultSet rs;

        stmt = connection.createStatement();
        String sql = "SELECT designs.design_name, designers.designer_initials, contracts.name_of_contract FROM designs, designers, contracts WHERE designs.contracts_id = contracts.contracts_id AND designs.designer_id = designers.designer_id;";
        rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String nameOfDesign = rs.getString("design_name");
            String nameOfDesigner = rs.getString("designer_initials");
            String nameOfContract = rs.getString("name_of_contract");

            AnchorPane designPane = createDesignPane(nameOfDesigner, nameOfDesign, nameOfContract);
            vbox.getChildren().add(designPane);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vbox);
        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(viewDesignsAnchorPane.getPrefHeight());
        scrollPane.setPrefWidth(viewDesignsAnchorPane.getPrefWidth());

        viewDesignsAnchorPane.getChildren().add(scrollPane);
        connection.close();
    }

    private AnchorPane createDesignPane(String designerName, String designName, String contractName) throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(388, 110);
        pane.setStyle("-fx-background-color: #C0C0C0; " +
                "-fx-border-color: #A0A0A0; " +
                "-fx-border-width: 1px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 1); " +
                "-fx-padding: 10;");

        File file = new File(paths.getImagesPath() + designName);
        Image fxImage = SwingFXUtils.toFXImage(imageSizeChanger.changeSize(file, 105, 105), null);

        ImageView imageView = new ImageView(fxImage);
        imageView.setFitHeight(105);
        imageView.setFitWidth(105);
        imageView.setLayoutX(20);
        imageView.setLayoutY(20);
        pane.getChildren().add(imageView);

        Label designerLabel = new Label(designerName);
        designerLabel.setLayoutX(135);
        designerLabel.setLayoutY(20);
        designerLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;");
        pane.getChildren().add(designerLabel);

        Label designLabel = new Label(designName);
        designLabel.setLayoutX(135);
        designLabel.setLayoutY(55);
        designLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");
        pane.getChildren().add(designLabel);

        Label contractLabel = new Label(contractName);
        contractLabel.setLayoutX(135);
        contractLabel.setLayoutY(90);
        contractLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
        pane.getChildren().add(contractLabel);

        return pane;
    }
}
