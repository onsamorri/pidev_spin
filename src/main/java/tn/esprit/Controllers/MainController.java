package tn.esprit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewInjuries(ActionEvent event) {
        loadScene("/ManageInjuries.fxml", event);
    }

    @FXML
    private void handleAddInjury(ActionEvent event) {
        loadScene("/AddInjury.fxml", event);
    }

    @FXML
    private void handleUpdateInjury(ActionEvent event) {
        loadScene("/UpdateInjury.fxml", event);
    }

    @FXML
    private void handleViewRecoveryPlans(ActionEvent event) {
        loadScene("/ManageRecoveryPlans.fxml", event);
    }

    @FXML
    private void handleAddRecoveryPlans(ActionEvent event) {
        loadScene("/AddRecoveryPlan.fxml", event);
    }

    @FXML
    private void handleUpdateRecoveryPlan(ActionEvent event) {
        loadScene("/UpdateRecoveryPlan.fxml", event);
    }
}
