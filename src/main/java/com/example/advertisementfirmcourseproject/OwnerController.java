package com.example.advertisementfirmcourseproject;

import SQL.InteractionsInterface.OwnerInteractions.SQLOwnerContractsInteractions;
import SQL.InteractionsInterface.SQLInteractions;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class OwnerController {
    private String initials;

    @FXML
    Label initialsLabel;

    @FXML
    Button exitBtn, viewCustomersBtn, viewContractsBtn, viewReportsBtn;

    @FXML
    AnchorPane viewAnchorPane;

    @FXML
    public void setInitials(String initials) {
        initialsLabel.setText("Вітаємо, " + initials + "!");
        initialsLabel.setWrapText(true);
        initialsLabel.setMaxWidth(550);
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
    private void onContractsViewBtn() throws SQLException, IOException {
        SQLInteractions sqlInteractions = new SQLOwnerContractsInteractions(viewAnchorPane);
        sqlInteractions.createView();
    }
}
