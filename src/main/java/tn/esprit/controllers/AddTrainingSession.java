package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.entities.Duration;
import tn.esprit.entities.Focus;
import tn.esprit.entities.TrainingSession;
import tn.esprit.services.QRCodeService;
import tn.esprit.services.TrainingSessionServices;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class AddTrainingSession {
    @FXML
    private ChoiceBox<Focus> training_focus;

    @FXML
    private ChoiceBox<String> training_duration;

    @FXML
    private TextField training_location;

    @FXML
    private TextField training_start_time;

    @FXML
    private TextField training_notes;

    @FXML
    private Button submitButton;

    @FXML
    private TableView<TrainingSession> trainingTable;

    @FXML
    private TableColumn<TrainingSession, Focus> colFocus;

    @FXML
    private TableColumn<TrainingSession, LocalTime> colStartTime;

    @FXML
    private TableColumn<TrainingSession, String> colDuration;

    @FXML
    private TableColumn<TrainingSession, String> colLocation;

    @FXML
    private TableColumn<TrainingSession, String> colNotes;

    @FXML
    private TableColumn<TrainingSession, Void> colActions;
    @FXML
    private ImageView backBtn;
    @FXML
    private Button weatherBtn;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;



    private final TrainingSessionServices trainingSessionService = new TrainingSessionServices();
    private final ObservableList<TrainingSession> trainingSessionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        backBtn.setOnMouseClicked(event -> switchBackToCoachFront());
        weatherBtn.setOnMouseClicked(event->switchScreenWeather());
        searchBtn.setOnAction(event -> search(searchField.getText()));
        colFocus.setCellValueFactory(new PropertyValueFactory<>("focus"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("session_notes"));

        trainingTable.setItems(trainingSessionList);
        updateTrainingSessionList();
        addActionButtonsToTable();

        // Populate the focus ChoiceBox with a placeholder
        ObservableList<Focus> focusOptions = FXCollections.observableArrayList(Focus.values());
        training_focus.setItems(focusOptions);
        training_focus.getItems().add(0, null);
        training_focus.setConverter(new StringConverter<>() {
            @Override
            public String toString(Focus focus) {
                return focus == null ? "Select Focus" : focus.name();
            }

            @Override
            public Focus fromString(String string) {
                return null;
            }
        });
        training_focus.setValue(null); // for the placeholder

        // Populate the duration ChoiceBox with a placeholder
        ObservableList<String> durationOptions = FXCollections.observableArrayList(
                Duration.FORTY_FIVE.getMinutes(),
                Duration.SIXTY.getMinutes(),
                Duration.NINETY.getMinutes(),
                Duration.ONE_TWENTY.getMinutes()
        );
        training_duration.setItems(durationOptions);
        training_duration.getItems().add(0, null);
        training_duration.setConverter(new StringConverter<>() {
            @Override
            public String toString(String duration) {
                return duration == null ? "Select Duration" : duration;
            }

            @Override
            // This to convert to the correct form of strings
            public String fromString(String string) {
                return null; //
            }
        });
        training_duration.setValue(null); // set the placeholder as the default value
    }

    @FXML
    private void switchScreenWeather() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/weather.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Weather Information");
            stage.setUserData(this);
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) weatherBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open weather screen: " + e.getMessage());
        }
    }


    private void switchBackToCoachFront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Coachfront.fxml"));
            Parent root = loader.load();

            Coachfront controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Welcome Coach");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) backBtn.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open coach front screen: " + e.getMessage());
        }
    }

    private void addActionButtonsToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(updateButton, deleteButton);

            {
                updateButton.setStyle("-fx-background-color: #BCCCE0; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #D68C45; -fx-text-fill: white;");
                pane.setSpacing(5);

                // Update Button Action
                updateButton.setOnAction(event -> {
                    TrainingSession trainingSession = (TrainingSession) getTableView().getItems().get(getIndex());
                    openUpdateTrainingSessionScreen(trainingSession);
                });

                // Delete Button Action
                deleteButton.setOnAction(event -> {
                    TrainingSession trainingSession = (TrainingSession) getTableView().getItems().get(getIndex());
                    deleteTrainingSession(trainingSession);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    @FXML
    void addTrainingSession(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Error", "Please correct the highlighted fields.");
            return;
        }

        try {
            Focus focus = training_focus.getValue();
            String duration = training_duration.getValue();
            String location = training_location.getText();
            LocalTime startTime = LocalTime.parse(training_start_time.getText()); // Parse the start_time
            String notes = training_notes.getText();

            TrainingSession trainingSession = new TrainingSession(focus, startTime, Duration.fromString(duration), location, notes);

            trainingSessionService.addP(trainingSession);

            showAlert("Success", "Training Session Added Successfully!");

            clearFields();

            updateTrainingSessionList();

        } catch (RuntimeException e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        boolean valid = true;

        // Validate focus
        if (training_focus.getValue() == null) {
            training_focus.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            training_focus.setStyle("");
        }
        // Validate start_time
        try {
            LocalTime.parse(training_start_time.getText());
            training_start_time.setBorder(Border.EMPTY);
        } catch (Exception e) {
            training_start_time.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        }

        // Validate duration
        if (training_duration.getValue() == null) {
            training_duration.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            training_duration.setStyle("");
        }

        // Validate location
        if (training_location.getText().isEmpty()) {
            training_location.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        } else {
            training_location.setBorder(Border.EMPTY);
        }

        // Validate notes
        if (training_notes.getText().isEmpty()) {
            training_notes.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            valid = false;
        } else {
            training_notes.setBorder(Border.EMPTY);
        }

        return valid;
    }

    private void clearFields() {
        training_focus.setValue(null);
        training_start_time.clear();
        training_duration.setValue(null);
        training_location.clear();
        training_notes.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void updateTrainingSessionList() {
        trainingSessionList.clear();
        try {
            List<TrainingSession> trainingSessions = trainingSessionService.returnList();
            trainingSessionList.addAll(trainingSessions);
            trainingTable.refresh();
        } catch (SQLException e) {
            showAlert("Error", "Failed to load training session data: " + e.getMessage());
        }
    }

    private void openUpdateTrainingSessionScreen(TrainingSession trainingSession) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateTrainingSession.fxml"));
            Parent root = loader.load();

            // Get the controller and initialize it with the selected training session
            UpdateTrainingSession controller = loader.getController();
            controller.initData(trainingSession);

            // Create a new stage for the update screen
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Training Session");
            stage.setUserData(this);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to open update screen: " + e.getMessage());
        }
    }

    private void deleteTrainingSession(TrainingSession trainingSession) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this record?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    trainingSessionService.delete(trainingSession);
                    trainingSessionList.remove(trainingSession);
                    showAlert("Success", "Training session deleted successfully!");
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete training session: " + e.getMessage());
                }
            }
        });
    }
    //search
    @FXML
    private void search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // If the keyword is empty, display the entire list
            trainingTable.setItems(trainingSessionList);
            return;
        }

        ObservableList<TrainingSession> filteredList = FXCollections.observableArrayList();
        keyword = keyword.toUpperCase(); // Convert keyword to uppercase for case-insensitive search

        for (TrainingSession session : trainingSessionList) {
            if (session.getFocus().name().contains(keyword) ||
                    session.getStart_time().toString().contains(keyword) ||
                    session.getDuration().name().contains(keyword) ||
                    session.getLocation().toLowerCase().contains(keyword.toLowerCase()) ||
                    session.getSession_notes().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(session);
            }
        }

        if (filteredList.isEmpty()) {
            // If no matches found, show an alert and refresh the table view
            showAlert("No Results", "No matching training sessions found.");
            trainingTable.setItems(trainingSessionList);
        } else {
            trainingTable.setItems(filteredList);
        }
    }



}