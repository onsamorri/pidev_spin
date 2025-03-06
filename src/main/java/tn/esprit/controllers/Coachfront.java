package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class Coachfront {

    @FXML private Label teamsBtn;
    @FXML private Label tournBtn;
    @FXML private Label performanceBtn;
    @FXML private Label TrainingSesh;
    @FXML private Label claimBtn;
    @FXML private Label to_Add_id;

    @FXML
    private Label deepseekBtn;



    @FXML
    public void initialize() {
        teamsBtn.setOnMouseClicked(event -> switchScreenTeam());
        tournBtn.setOnMouseClicked(event -> switchScreenTournament());
        performanceBtn.setOnMouseClicked(event -> switchScreenPerformance());
        TrainingSesh.setOnMouseClicked(event -> switchScreenTraining());
        //claimBtn.setOnMouseClicked(event -> switchScreenClaim());
        //to_Add_id.setOnMouseClicked(event -> switchScreenAthlete());
        deepseekBtn.setOnMouseClicked(event ->openDeepSeek());

    }

    //Asma (team and tournament)
    private void switchScreenTeam() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTeam.fxml"));
            Parent root = loader.load();

            addTeam controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding Team");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) teamsBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open add screen: " + e.getMessage());
        }
    }
    private void switchScreenTournament() {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTournament.fxml"));
            Parent root = loader.load();

            addTournament controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding Tournament");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) tournBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open tournament screen: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // joujou (Performance and training session and deepseek)

    private void switchScreenPerformance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPerformance.fxml"));
            Parent root = loader.load();

            AddPerformanceData controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding Performance");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) tournBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Performance screen " + e.getMessage());
        }
    }

    private void switchScreenTraining() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTrainingSession.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding Training Session");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) teamsBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Training Session screen: " + e.getMessage());
        }
    }

    private void openDeepSeek() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deepseek.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("DeepSeek Conversation");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) deepseekBtn.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            showAlert("Error", "Failed to open DeepSeek: " + e.getMessage());
        }
    }
//    //Yassine (Claim)
//    private void switchScreenClaim() {
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddClaimInterface.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Adding Claim");
//            stage.setUserData(this);
//            stage.show();
//            Stage currentStage = (Stage) claimBtn.getScene().getWindow();
//            currentStage.close();
//        } catch (IOException e) {
//            showAlert("Error", "Failed to open Training Session screen: " + e.getMessage());
//        }
//    }
//    //Ons (athelete)
//    private void switchScreenAthlete() {
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addAthlete.fxml"));
//            Parent root = loader.load();
//
//            addAthlete controller = loader.getController();
//            controller.initialize();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Adding Athlete");
//            stage.setUserData(this);
//            stage.show();
//            Stage currentStage = (Stage) to_Add_id.getScene().getWindow();
//            currentStage.close();
//        } catch (IOException e) {
//            showAlert("Error", "Failed to open add screen: " + e.getMessage());
//        }
//    }



}
