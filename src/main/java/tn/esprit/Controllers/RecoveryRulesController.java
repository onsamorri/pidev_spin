package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tn.esprit.services.ExerciseServices;

import java.io.IOException;
import java.util.List;

public class RecoveryRulesController {

    @FXML private TextField exerciseInputField;
    @FXML private Button chatBotButton;
    @FXML private Button fetchExerciseButton;
    @FXML private Button YoutubeButton;
    @FXML private Button MyInjuriesButton;
    @FXML private ListView<String> exerciseListView;
    @FXML private Label exerciseDetailsLabel;
    private ExerciseServices exerciseServices = new ExerciseServices();

    @FXML
    public void initialize() {
        chatBotButton.setOnMouseClicked(event -> handleChatBotClick());
        YoutubeButton.setOnMouseClicked(event -> handleYouTubeClick());
        MyInjuriesButton.setOnMouseClicked(event -> switchScreenToMyInjuries());
        fetchExerciseButton.setOnAction(this::onExerciseSubmit);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleChatBotClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecoveryChatBot.fxml"));
            Pane root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleYouTubeClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/YoutubeSearch.fxml"));
            Parent root = loader.load();
            YouTubeSearchController controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("YouTube Screen");
            stage.show();

            Stage currentStage = (Stage) YoutubeButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open YouTube screen: " + e.getMessage());
        }
    }

    @FXML
    private void switchScreenToMyInjuries() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MyInjuries.fxml"));
            Parent root = loader.load();
            MyInjuriesController controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View My Injuries");
            stage.show();

            Stage currentStage = (Stage) MyInjuriesButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open My Injuries screen: " + e.getMessage());
        }
    }

    @FXML
    public void onExerciseSubmit(ActionEvent event) {
        String exerciseType = exerciseInputField.getText();
        if (exerciseType.isEmpty()) {
            exerciseDetailsLabel.setText("Please enter an exercise.");
            return;
        }

        // Fetch exercise data from the ExerciseDB API (via Wger API)
        String response = exerciseServices.fetchExerciseData();
        List<String> exercises = exerciseServices.parseExerciseData(response);

        exerciseListView.getItems().clear();

        if (exercises.isEmpty()) {
            exerciseDetailsLabel.setText("No exercises found.");
        } else {
            for (String exercise : exercises) {
                exerciseListView.getItems().add(exercise);
            }
        }
    }

    @FXML
    public void onExerciseSelected(MouseEvent event) {
        String selectedExerciseName = exerciseListView.getSelectionModel().getSelectedItem();
        if (selectedExerciseName != null) {
            // Assuming the ExerciseServices fetches exercise details by name
            String exerciseDetailsResponse = exerciseServices.fetchExerciseDetailsFromAPI(selectedExerciseName);
            String exerciseDetails = exerciseServices.parseExerciseDetails(exerciseDetailsResponse);

            if (exerciseDetails != null) {
                exerciseDetailsLabel.setText("Exercise: " + selectedExerciseName + "\nDetails: " + exerciseDetails);
            } else {
                exerciseDetailsLabel.setText("No details found for this exercise.");
            }
        }
    }
}
