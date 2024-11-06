package SQL.InteractionsInterface.CustomerInteractions;

import FileInfoGetters.FilesInfoGetter;
import Paths.Paths;
import SQL.InteractionsInterface.SQLInteractions;
import SQL.PostgreSQLConnection;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLCustomerContractsInteractions implements SQLInteractions {
    private final PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
    private final VBox vbox = new VBox(10);
    private final AnchorPane viewAnchorPane;
    private final String initials;

    public SQLCustomerContractsInteractions(AnchorPane viewAnchorPane, String initials) {
        this.viewAnchorPane = viewAnchorPane;
        this.initials = initials;
    }

    @Override
    public void createView() throws SQLException, IOException {
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

        scrollPane.setPrefHeight(viewAnchorPane.getPrefHeight());
        scrollPane.setPrefWidth(viewAnchorPane.getPrefWidth());

        viewAnchorPane.getChildren().add(scrollPane);
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
        employeeInitialsLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 16px;");
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

        FilesInfoGetter filesInfoGetter = new FilesInfoGetter();
        Paths paths = new Paths();

        TextFlow contractInfoTF = new TextFlow();
        Text text = new Text(filesInfoGetter.getContent(paths.getContractsPath(), contractName));
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
