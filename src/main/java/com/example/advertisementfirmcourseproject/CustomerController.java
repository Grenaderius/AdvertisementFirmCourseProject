package com.example.advertisementfirmcourseproject;

import FileInfoGetters.ContractInfoGetter;
import ImageChangers.ImageSizeChanger;
import Paths.Paths;
import SQL.PostgreSQLConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerController {
    private final PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
    private final Paths paths = new Paths();
    private final ImageSizeChanger imageSizeChanger = new ImageSizeChanger();

    private String initials;

    @FXML
    Label helloLabel;

    @FXML
    Button exitBtn, viewDesignsBtn, viewContractsBtn;

    @FXML
    AnchorPane viewDesignsAnchorPane;

    @FXML
    VBox vbox;

    public void setInitials(String initials) {
        helloLabel.setText("Вітаємо, " + initials + "!");
        helloLabel.setWrapText(true);
        helloLabel.setMaxWidth(550);
        this.initials = initials;
    }

    @FXML
    private void exit() throws IOException {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();

        HelloApplication helloApplication = new HelloApplication();
        helloApplication.start(stage);
    }

    @FXML
    private void viewDesignsBtn() throws SQLException, IOException {
        createDesignsView();
    }

    @FXML
    private void viewContractsBtn() throws SQLException, IOException {
        createContractsView();
    }

    private void createDesignsView() throws SQLException, IOException {
        vbox = new VBox(10);
        vbox.setPrefWidth(388);
        vbox.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");

        Connection connection = postgreSQLConnection.connect();
        Statement stmtForId;
        ResultSet rsForId;

        stmtForId = connection.createStatement();
        String sql = "SELECT contracts.contracts_id FROM contracts, customers WHERE customers.customer_id = contracts.customer_id AND customers.customer_initials = '" + initials +"';";
        rsForId = stmtForId.executeQuery(sql);

        while (rsForId.next()) {
            String contractId = rsForId.getString("contracts_id");
            designsInfoGetter(connection, contractId);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vbox);
        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(viewDesignsAnchorPane.getPrefHeight());
        scrollPane.setPrefWidth(viewDesignsAnchorPane.getPrefWidth());

        viewDesignsAnchorPane.getChildren().add(scrollPane);
        connection.close();
    }

    private void designsInfoGetter(Connection connection, String contractId) throws SQLException, IOException {
        Statement stmtForDesigns;
        ResultSet rsForDesigns;

        stmtForDesigns = connection.createStatement();
        String sql = "SELECT designs.design_name, designers.designer_initials, contracts.name_of_contract FROM designs, designers, contracts WHERE designs.contracts_id = contracts.contracts_id AND designs.designer_id = designers.designer_id AND designs.contracts_id = " + contractId + ";";
        rsForDesigns = stmtForDesigns.executeQuery(sql);

        while (rsForDesigns.next()) {
            String nameOfDesign = rsForDesigns.getString("design_name");
            String nameOfDesigner = rsForDesigns.getString("designer_initials");
            String nameOfContract = rsForDesigns.getString("name_of_contract");

            AnchorPane designPane = createDesignPane(nameOfDesigner, nameOfDesign, nameOfContract);
            vbox.getChildren().add(designPane);
        }
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

    private void createContractsView() throws SQLException, IOException {
        vbox = new VBox(10);
        vbox.setPrefWidth(388);
        vbox.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");

        Connection connection = postgreSQLConnection.connect();
        Statement stmtForId;
        ResultSet rsForId;

        stmtForId = connection.createStatement();
        String sql = "SELECT contracts.contracts_id FROM contracts, customers WHERE customers.customer_id = contracts.customer_id AND customers.customer_initials = '" + initials +"';";
        rsForId = stmtForId.executeQuery(sql);

            while (rsForId.next()) {
                String contractId = rsForId.getString("contracts_id");
                contractsInfoGetter(connection, contractId);
            }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vbox);
        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(viewDesignsAnchorPane.getPrefHeight());
        scrollPane.setPrefWidth(viewDesignsAnchorPane.getPrefWidth());

        viewDesignsAnchorPane.getChildren().add(scrollPane);
        connection.close();
    }

    private void contractsInfoGetter(Connection connection, String contractId) throws SQLException, IOException {
        Statement stmtForDesigns;
        ResultSet rsForDesigns;

        stmtForDesigns = connection.createStatement();
        String sql = "Select contracts.name_of_contract, employees.employee_initials, contracts.concluding_date, contracts.termination_date from contracts, employees where contracts.employee_id = employees.employee_id and " + contractId + " = contracts.contracts_id;";
        rsForDesigns = stmtForDesigns.executeQuery(sql);

        while (rsForDesigns.next()) {
            String nameOfContract = rsForDesigns.getString("name_of_contract");
            String employeeInitials = rsForDesigns.getString("employee_initials");
            String concludingDate = rsForDesigns.getString("concluding_date");
            String terminationDate = rsForDesigns.getString("termination_date");

            AnchorPane designPane = createContractsPane(nameOfContract, employeeInitials, concludingDate, terminationDate);
            vbox.getChildren().add(designPane);
        }
    }

    private AnchorPane createContractsPane(String contractName, String employeeInitials, String concludingDate, String terminationDate) throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(388, 110);
        pane.setStyle("-fx-background-color: #C0C0C0; " +
                "-fx-border-color: #A0A0A0; " +
                "-fx-border-width: 1px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 1); " +
                "-fx-padding: 10;");

        Label contractNameLabel = new Label(contractName.substring(0, contractName.length() - 4));
        contractNameLabel.setLayoutX(10);
        contractNameLabel.setLayoutY(0);
        contractNameLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 24px; -fx-font-weight: bold;");
        contractNameLabel.setWrapText(true);
        contractNameLabel.setMaxWidth(368);
        pane.getChildren().add(contractNameLabel);

        Label employeeInitialsLabel = new Label(employeeInitials);
        employeeInitialsLabel.setLayoutX(10);
        employeeInitialsLabel.setLayoutY(24);
        employeeInitialsLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 18px;");
        contractNameLabel.setWrapText(true);
        contractNameLabel.setMaxWidth(368);
        pane.getChildren().add(employeeInitialsLabel);

        Label concludingDateLabel = new Label(concludingDate);
        concludingDateLabel.setLayoutX(10);
        concludingDateLabel.setLayoutY(47);
        concludingDateLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        pane.getChildren().add(concludingDateLabel);

        Label terminationDateLabel = new Label(terminationDate);
        terminationDateLabel.setLayoutX(10);
        terminationDateLabel.setLayoutY(62);
        terminationDateLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        pane.getChildren().add(terminationDateLabel);


        Label contractLabel = new Label("Договір:");
        contractLabel.setLayoutX(147);
        contractLabel.setLayoutY(75);
        contractLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;");
        contractLabel.setMaxWidth(368);
        pane.getChildren().add(contractLabel);

        ContractInfoGetter contractInfoGetter = new ContractInfoGetter(contractName);

        TextFlow contractInfoTF = new TextFlow();
        Text text = new Text(contractInfoGetter.getContractContent());
        text.setStyle("-fx-fill: #333333; -fx-font-size: 14px;");
        contractInfoTF.getChildren().add(text);

        contractInfoTF.setPrefWidth(330);
        contractInfoTF.setMaxWidth(330);

        VBox textContainer = new VBox();
        textContainer.getChildren().add(contractInfoTF);

        AnchorPane.setTopAnchor(textContainer, 80.0);
        AnchorPane.setLeftAnchor(textContainer, 0.0);
        AnchorPane.setRightAnchor(textContainer, 0.0);
        AnchorPane.setBottomAnchor(textContainer, 0.0);

        pane.getChildren().add(textContainer);
        return pane;
    }
}
