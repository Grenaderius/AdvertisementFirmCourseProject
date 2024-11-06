package SQL.InteractionsInterface.OwnerInteractions;

import SQL.InteractionsInterface.SQLInteractions;
import SQL.PostgreSQLConnection;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLOwnerCustomerInteractions  implements SQLInteractions {
    private final PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
    private final VBox vbox = new VBox(10);
    private final AnchorPane viewAnchorPane;

    public SQLOwnerCustomerInteractions(AnchorPane viewAnchorPane) {
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

    private void infoGetter(Connection connection) throws SQLException{
        Statement stmtForDesigns;
        ResultSet rs;

        stmtForDesigns = connection.createStatement();
        String sql = """
                select\s
                customers.customer_initials,\s
                count(contracts.customer_id) as count_of_contracts
                from\s
                customers
                left join\s
                contracts on customers.customer_id = contracts.customer_id
                group by\s
                customers.customer_id, customers.customer_initials;""";

            rs = stmtForDesigns.executeQuery(sql);

            while (rs.next()) {
                String customerInitials = rs.getString("customer_initials");
                String countOfContracts = rs.getString("count_of_contracts");

                AnchorPane customersPane = createCustomersPane(customerInitials, countOfContracts);
                vbox.getChildren().add(customersPane);
            }
    }

    private AnchorPane createCustomersPane(String customerInitials, String count_of_contracts){
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(388, 20);
        pane.setStyle("-fx-background-color: #C0C0C0; " +
                "-fx-border-color: #A0A0A0; " +
                "-fx-border-width: 1px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 5, 0, 0, 1); " +
                "-fx-padding: 10;");

        Label customerInitialsLabel = new Label(customerInitials);
        customerInitialsLabel.setLayoutX(10);
        customerInitialsLabel.setLayoutY(5);
        customerInitialsLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 16px; -fx-font-weight: bold;");
        customerInitialsLabel.setWrapText(true);
        customerInitialsLabel.setMaxWidth(368);
        pane.getChildren().add(customerInitialsLabel);

        Label countOfContractsLabel = new Label("Кількість договорів:" + count_of_contracts);
        countOfContractsLabel.setLayoutX(10);
        countOfContractsLabel.setLayoutY(30);
        countOfContractsLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;");
        pane.getChildren().add(countOfContractsLabel);

        AnchorPane.setBottomAnchor(countOfContractsLabel, 0.0);
        return pane;
    }
}

