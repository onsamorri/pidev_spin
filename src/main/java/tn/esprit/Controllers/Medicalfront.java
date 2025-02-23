package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class Medicalfront {
    @FXML private Label claimButton;
    @FXML
    public void initialize() {

        claimButton.setOnMouseClicked(event -> switchScreenClaim3());

    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void switchScreenClaim3() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddClaimMedical.fxml"));
            Parent root = loader.load();

            AddClaimMedical controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding Claim");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) claimButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Training Session screen: " + e.getMessage());
        }
    }



}
