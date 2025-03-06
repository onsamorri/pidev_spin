package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Athletefront {
    @FXML
    private Label claimButton;

    @FXML
    private Label TrainingSeshAthleteBtn;






    @FXML
    public void initialize() {
        // Set action for claimButton
        claimButton.setOnMouseClicked(event -> switchScreenClaim2());

        TrainingSeshAthleteBtn.setOnMouseClicked(event -> switchScreenShowTrainingSessionFront());


    }
    //Trainingsession
    private void switchScreenShowTrainingSessionFront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AthleteTrainingSessionFront.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Training Sessions");
            stage.setUserData(this);
            stage.show();

            // Close current stage
            Stage currentStage = (Stage) TrainingSeshAthleteBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Training Session screen: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Switch to Add Claim screen (already exists)
    private void switchScreenClaim2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddClaimAthlete.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding Claim");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) claimButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Claim screen: " + e.getMessage());
        }
    }



}
