package SQL.InteractionsInterface.OwnerInteractions;

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

public class SQLOwnerReportsInteractions  implements SQLInteractions {
    private final PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
    private final VBox vbox = new VBox(10);
    private final AnchorPane viewAnchorPane;

    public SQLOwnerReportsInteractions(AnchorPane viewAnchorPane) {
        this.viewAnchorPane = viewAnchorPane;
    }

    @Override
    public void createView() throws SQLException, IOException {
        vbox.setPrefWidth(388);
        vbox.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");

        Connection connection = postgreSQLConnection.connect();
        connection.createStatement();
        infoGetter(connection);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vbox);
        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(viewAnchorPane.getPrefHeight());
        scrollPane.setPrefWidth(viewAnchorPane.getPrefWidth());

        viewAnchorPane.getChildren().add(scrollPane);
        connection.close();
    }

    private void infoGetter(Connection connection) throws SQLException, IOException {
        Statement stmtForDesigns;
        ResultSet rs;

        stmtForDesigns = connection.createStatement();
        String sql = """
                SELECT\s
                    reports.report_name, reports.employee_id, reports.report_date,\s
                    COALESCE(employees.employee_initials, manager.manager_initials) AS initials
                FROM\s
                    reports
                LEFT JOIN\s
                    employees ON reports.employee_id = employees.employee_id\s
                LEFT JOIN\s
                    manager ON reports.manager_id = manager.manager_id\s""";
        rs = stmtForDesigns.executeQuery(sql);

        while (rs.next()) {
            String nameOfReport = rs.getString("report_name");
            String reportDate = rs.getString("report_date");
            String workerInitials = rs.getString("initials");

            String employeeId = rs.getString("employee_id");
            String workerType = employeeId == null ? "Працівник: " : "Менеджер: ";

            AnchorPane designPane = createReportsPane(nameOfReport, workerInitials, reportDate, workerType);
            vbox.getChildren().add(designPane);
        }
    }

    private AnchorPane createReportsPane(String nameOfReport, String workerInitials, String reportDate, String workerType) throws IOException {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(388, 110);
        pane.setStyle("-fx-background-color: #C0C0C0; " +
                "-fx-border-color: #A0A0A0; " +
                "-fx-border-width: 1px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 1); " +
                "-fx-padding: 10;");

        Label reportNameLabel = new Label(nameOfReport.substring(0, nameOfReport.length() - 4));
        reportNameLabel.setLayoutX(10);
        reportNameLabel.setLayoutY(0);
        reportNameLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 24px; -fx-font-weight: bold;");
        reportNameLabel.setWrapText(true);
        reportNameLabel.setMaxWidth(368);
        pane.getChildren().add(reportNameLabel);

        Label employeeInitialsLabel = new Label(workerType + workerInitials);
        employeeInitialsLabel.setLayoutX(10);
        employeeInitialsLabel.setLayoutY(30);
        employeeInitialsLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 16px;");
        employeeInitialsLabel.setWrapText(true);
        employeeInitialsLabel.setMaxWidth(368);
        pane.getChildren().add(employeeInitialsLabel);

        Label concludingDateLabel = new Label(reportDate);
        concludingDateLabel.setLayoutX(10);
        concludingDateLabel.setLayoutY(55);
        concludingDateLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        pane.getChildren().add(concludingDateLabel);

        Label contractLabel = new Label("Звіт:");
        contractLabel.setLayoutX(160);
        contractLabel.setLayoutY(65);
        contractLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;");
        contractLabel.setMaxWidth(368);
        pane.getChildren().add(contractLabel);

        FilesInfoGetter filesInfoGetter = new FilesInfoGetter();
        Paths paths = new Paths();

        TextFlow contractInfoTF = new TextFlow();
        Text text = new Text(filesInfoGetter.getContent(paths.getReportsPath(), nameOfReport));
        text.setStyle("-fx-fill: #333333; -fx-font-size: 14px;");
        contractInfoTF.getChildren().add(text);

        contractInfoTF.setPrefWidth(330);
        contractInfoTF.setMaxWidth(330);

        VBox textContainer = new VBox();
        textContainer.getChildren().add(contractInfoTF);

        AnchorPane.setTopAnchor(textContainer, 70.0);
        AnchorPane.setLeftAnchor(textContainer, 0.0);
        AnchorPane.setRightAnchor(textContainer, 0.0);
        AnchorPane.setBottomAnchor(textContainer, 0.0);

        pane.getChildren().add(textContainer);
        return pane;
    }
}
