package com.example.advertisementfirmcourseproject.UserControllers;

import SQL.InteractionsInterface.CustomerInteractions.SQLCustomerContractsInteractions;
import SQL.InteractionsInterface.CustomerInteractions.SQLCustomerDesignInteractions;
import SQL.InteractionsInterface.SQLInteractions;
import com.example.advertisementfirmcourseproject.AdvertisementFirm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerController {
    private SQLInteractions sqlInteractions;
    private String initials;

    @FXML
    Label helloLabel;

    @FXML
    Button exitBtn, viewDesignsBtn, viewContractsBtn;

    @FXML
    AnchorPane viewDesignsAnchorPane;

    public void setInitials(String initials) {
        helloLabel.setText("Вітаємо в акаунті замовника, " + initials + "!");
        helloLabel.setWrapText(true);
        helloLabel.setMaxWidth(550);
        this.initials = initials;
    }

    @FXML
    private void exit() throws IOException {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();

        AdvertisementFirm advertisementFirm = new AdvertisementFirm();
        advertisementFirm.start(stage);
    }

    @FXML
    private void viewDesignsBtn() throws SQLException, IOException {
        sqlInteractions = new SQLCustomerDesignInteractions(viewDesignsAnchorPane, initials);
        sqlInteractions.createView();
    }

    @FXML
    private void viewContractsBtn() throws SQLException, IOException {
        sqlInteractions = new SQLCustomerContractsInteractions(viewDesignsAnchorPane, initials);
        sqlInteractions.createView();
    }
}
