package tn.esprit.controllers;

import com.jfoenix.controls.JFXButton;
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
import tn.esprit.services.FavoriteExercises;

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
    @FXML
    private JFXButton favoriteButton;
    @FXML private ListView<String> favoriteExercisesListView; // Added for favorite exercises display
    private FavoriteExercises favoriteExercises = new FavoriteExercises();

    @FXML
    public void initialize() {
        chatBotButton.setOnMouseClicked(event -> handleChatBotClick());
        YoutubeButton.setOnMouseClicked(event -> handleYouTubeClick());
        MyInjuriesButton.setOnMouseClicked(event -> switchScreenToMyInjuries());
        fetchExerciseButton.setOnAction(this::onExerciseSubmit);

        updateFavoriteExercisesList(); // Populate the favorite exercises list at initialization
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
    private void toggleFavorite(ActionEvent event) {
        String selectedExercise = exerciseListView.getSelectionModel().getSelectedItem();

        if (selectedExercise != null) {
            if (favoriteExercises.getFavoriteExercises().contains(selectedExercise)) {
                // Remove from favorites
                favoriteExercises.removeFavoriteExercise(selectedExercise);
                favoriteButton.setText("❤️"); // Empty heart icon (emoji)
                showAlert("Exercise Removed", selectedExercise + " has been removed from your favorites.");
            } else {
                // Add to favorites
                favoriteExercises.addFavoriteExercise(selectedExercise);
                favoriteButton.setText("🖤"); // Filled heart icon (emoji)
                showAlert("Exercise Added", selectedExercise + " has been added to your favorites!");
            }

            // Update the favorites list view
            updateFavoriteExercisesList();
        } else {
            showAlert("No Exercise Selected", "Please select an exercise to toggle favorites.");
        }
    }

    private void updateFavoriteExercisesList() {
        // Fetch the updated list of favorite exercises
        List<String> favoriteExercisesList = favoriteExercises.getFavoriteExercises();

        // Clear the list view first to ensure we start fresh
        favoriteExercisesListView.getItems().clear();

        // If the favorite exercises list is not empty, add the updated list to the list view
        if (!favoriteExercisesList.isEmpty()) {
            favoriteExercisesListView.getItems().addAll(favoriteExercisesList);
        } else {
            // If no exercises are in favorites, show a message indicating the list is empty
            favoriteExercisesListView.getItems().add("No favorite exercises yet.");
        }
    }

    public List<String> getFavoriteExercises() {
        return favoriteExercises.getFavoriteExercises();
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
        String response = ExerciseServices.fetchExerciseData();
        List<String> exercises = ExerciseServices.parseExerciseData(response);

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
            String exerciseDetailsResponse = ExerciseServices.fetchExerciseDetailsFromAPI(selectedExerciseName);
            String exerciseDetails = ExerciseServices.parseExerciseDetails(exerciseDetailsResponse);

            exerciseDetailsLabel.setText("Exercise: " + selectedExerciseName + "\nDetails: " + exerciseDetails);
        }
    }
}
