package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Injury;
import tn.esprit.entities.RecoveryPlan;
import tn.esprit.entities.user;
import tn.esprit.services.RecoveryRulesChatBotServices;
import tn.esprit.controllers.SessionManager;

import java.io.IOException;
import java.util.List;

public class MyInjuriesController {

    @FXML
    private VBox injuryListVBox;
    @FXML
    private Label BackButton;

    private user currentUser = SessionManager.getInstance().getAuthenticatedUser();
    private RecoveryRulesChatBotServices recoveryService = new RecoveryRulesChatBotServices();

    public void initialize() {
        loadUserInjuriesAndRecoveryPlans();

        BackButton.setOnMouseClicked(event -> switchScreenToBack());
    }

    private void switchScreenToBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Athletefront.fxml"));
            Parent root = loader.load();

            Athletefront controller = loader.getController();
            controller.initialize();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Back");
            stage.show();

            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Athletefront screen: " + e.getMessage());
        }
    }

    private void loadUserInjuriesAndRecoveryPlans() {
        if (currentUser == null) {
            showAlert("Error", "No authenticated user found.");
            return;
        }

        try {
            // Fetch injuries and recovery plans using services
            List<Injury> injuries = recoveryService.getInjuriesByUserName(currentUser.getUser_fname(), currentUser.getUser_lname());
            List<RecoveryPlan> recoveryPlans = recoveryService.getRecoveryPlansByUserName(currentUser.getUser_fname(), currentUser.getUser_lname());

            // Clear previous content
            injuryListVBox.getChildren().clear();

            // Load injury data dynamically
            for (Injury injury : injuries) {
                HBox injuryBox = createInjuryBox(injury);
                injuryListVBox.getChildren().add(injuryBox);
            }

            // Load recovery plans dynamically
            for (RecoveryPlan recoveryPlan : recoveryPlans) {
                HBox recoveryPlanBox = createRecoveryPlanBox(recoveryPlan);
                injuryListVBox.getChildren().add(recoveryPlanBox);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load injuries and recovery plans: " + e.getMessage());
        }
    }

    private HBox createInjuryBox(Injury injury) {
        HBox injuryBox = new HBox(10);
        injuryBox.setStyle("-fx-background-color: white; -fx-background-radius: 30px; -fx-padding: 10;");
        injuryBox.setPrefHeight(114);
        injuryBox.setPrefWidth(559);

        ImageView injuryImage = new ImageView(new Image(getClass().getResource("/injuryIcon2.png").toExternalForm()));
        injuryImage.setFitHeight(50);
        injuryImage.setFitWidth(50);

        VBox injuryDetails = new VBox(5);
        Label injuryTypeLabel = new Label("Injury Type: " + injury.getInjuryType());
        Label injuryDateLabel = new Label("Date: " + injury.getInjuryDate());
        Label injurySeverityLabel = new Label("Severity: " + injury.getInjury_severity());
        injuryDetails.getChildren().addAll(injuryTypeLabel, injuryDateLabel, injurySeverityLabel);

        injuryBox.getChildren().addAll(injuryImage, injuryDetails);
        return injuryBox;
    }

    private HBox createRecoveryPlanBox(RecoveryPlan recoveryPlan) {
        HBox recoveryPlanBox = new HBox(10);
        recoveryPlanBox.setStyle("-fx-background-color: white; -fx-background-radius: 30px; -fx-padding: 10;");
        recoveryPlanBox.setPrefHeight(110);
        recoveryPlanBox.setPrefWidth(500);

        ImageView recoveryPlanImage = new ImageView(new Image(getClass().getResource("/recoveryPlanIcon.png").toExternalForm()));
        recoveryPlanImage.setFitHeight(50);
        recoveryPlanImage.setFitWidth(50);

        VBox recoveryDetails = new VBox(5);
        Label recoveryGoalLabel = new Label("Recovery Goal: " + recoveryPlan.getRecovery_Goal().toString());
        Label recoveryStartDateLabel = new Label("Start Date: " + recoveryPlan.getRecovery_StartDate());
        Label recoveryEndDateLabel = new Label("End Date: " + recoveryPlan.getRecovery_EndDate());
        Label statusLabel = new Label("Status: " + recoveryPlan.getRecovery_Status());

        recoveryDetails.getChildren().addAll(recoveryGoalLabel, recoveryStartDateLabel, recoveryEndDateLabel, statusLabel);

        recoveryPlanBox.getChildren().addAll(recoveryPlanImage, recoveryDetails);
        return recoveryPlanBox;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
