package SQL.InteractionsInterface.DesignerInteractions;

import ImageChangers.ImageSizeChanger;
import Paths.Paths;
import SQL.InteractionsInterface.SQLInteractions;
import SQL.PostgreSQLConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDesignerInteractions implements SQLInteractions {
    private final PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
    private final ImageSizeChanger imageSizeChanger = new ImageSizeChanger();
    private final Paths paths = new Paths();
    private final VBox vbox = new VBox(10);
    private final AnchorPane viewAnchorPane;

    public SQLDesignerInteractions(AnchorPane viewAnchorPane) {
        this.viewAnchorPane = viewAnchorPane;
    }

    @Override
    public void createView() throws SQLException, IOException {
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

        scrollPane.setPrefHeight(viewAnchorPane.getPrefHeight());
        scrollPane.setPrefWidth(viewAnchorPane.getPrefWidth());

        viewAnchorPane.getChildren().add(scrollPane);
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
