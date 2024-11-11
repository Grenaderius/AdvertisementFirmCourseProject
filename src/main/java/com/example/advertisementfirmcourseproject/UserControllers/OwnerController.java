package com.example.advertisementfirmcourseproject.UserControllers;

import SQL.InteractionsInterface.OwnerInteractions.SQLOwnerContractsInteractions;
import SQL.InteractionsInterface.OwnerInteractions.SQLOwnerCustomerInteractions;
import SQL.InteractionsInterface.OwnerInteractions.SQLOwnerReportsInteractions;
import SQL.InteractionsInterface.SQLInteractions;
import com.example.advertisementfirmcourseproject.AdvertisementFirm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class OwnerController {
    private SQLInteractions sqlInteractions;
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

        AdvertisementFirm advertisementFirm = new AdvertisementFirm();
        advertisementFirm.start(stage);
    }

    @FXML
    private void onContractsViewBtn() throws SQLException, IOException {
        sqlInteractions = new SQLOwnerContractsInteractions(viewAnchorPane);
        sqlInteractions.createView();
    }

    @FXML
    private void onCustomersViewBtn() throws SQLException, IOException {
        sqlInteractions = new SQLOwnerCustomerInteractions(viewAnchorPane);
        sqlInteractions.createView();
    }

    @FXML
    private void onReportsViewBtn() throws SQLException, IOException {
        sqlInteractions = new SQLOwnerReportsInteractions(viewAnchorPane);
        sqlInteractions.createView();
    }


}
