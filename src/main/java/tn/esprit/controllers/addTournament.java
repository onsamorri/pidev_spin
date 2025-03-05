package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.entities.results;
import tn.esprit.entities.team;
import tn.esprit.entities.tournament;
import tn.esprit.services.TournamentService;
import tn.esprit.services.resultsServices;
import tn.esprit.services.teamServices;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import netscape.javascript.JSObject;

public class addTournament {
    @FXML
    private WebView map;

    @FXML
    private Label addTeamLabel;

    @FXML
    private Label addTournLabel;

    @FXML
    private Button addTournbtn;

    @FXML
    private Label consultTeamLabel;

    @FXML
    private TableView<team> tabTeam;

    @FXML
    private TableColumn<team, Void> tabTeamActions;

    @FXML
    private TableColumn<team, Integer> tabTeamId;

    @FXML
    private TableColumn<team, Integer> tabTeamLosses;

    @FXML
    private TableColumn<team, String> tabTeamName;

    @FXML
    private TableColumn<team, Integer> tabTeamNath;

    @FXML
    private TableColumn<team, String> tabTeamSport;

    @FXML
    private TableColumn<team, Integer> tabTeamWins;

    @FXML
    private DatePicker tournEndDate;

    @FXML
    private TextField tournLoc;

    @FXML
    private TextField tournName;

    @FXML
    private DatePicker tournStartDate;

    @FXML
    private ComboBox<String> tournTOS;

    @FXML
    private Label viewTournLabel;
    @FXML
    private Label backBtn;

    private final TournamentService tournamentService = new TournamentService();
    private final teamServices teamService = new teamServices();
    private final resultsServices resultsService = new resultsServices();
    private final ObservableList<team> teamList = FXCollections.observableArrayList();
    private int selectedTeamsCount = 0; // Counter for added teams
    private JavaConnector javaConnector; // Strong reference to JavaConnector
    private final ObservableList<team> selectedTeams = FXCollections.observableArrayList(); // List to store selected teams

    @FXML
    public void initialize() {
        addTournbtn.setOnAction(event -> addTournAction());
        viewTournLabel.setOnMouseClicked(event -> switchScreenConsult());
        backBtn.setOnMouseClicked(event -> switchBackToCoachFront());
        // Update event trigger: Filtering when ComboBox is clicked
        tournTOS.setOnAction(event -> filterTeamsBySport());


        // Set up table columns
        tabTeamId.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        tabTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        tabTeamNath.setCellValueFactory(new PropertyValueFactory<>("teamNath"));
        tabTeamWins.setCellValueFactory(new PropertyValueFactory<>("teamW"));
        tabTeamLosses.setCellValueFactory(new PropertyValueFactory<>("teamL"));
        tabTeamSport.setCellValueFactory(new PropertyValueFactory<>("teamTOS"));
        tabTeam.setItems(teamList);

        addActionButtonsToTable();
        viewTournLabel.setOnMouseClicked(event -> switchScreenConsult());
        loadMap();
        WebEngine webEngine = map.getEngine();
        webEngine.setOnAlert(event -> System.out.println("WebView Alert: " + event.getData()));
        webEngine.setOnError(event -> System.err.println("WebView Error: " + event.getMessage()));

        webEngine.load(getClass().getResource("/map.html").toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // Re-inject JavaConnector
                JSObject window = (JSObject) webEngine.executeScript("window");
                javaConnector = new JavaConnector(); // Store the JavaConnector object
                window.setMember("javaConnector", javaConnector);

                webEngine.executeScript("console.log('Calling reconnectJavaConnector from Java...');");
                webEngine.executeScript("reconnectJavaConnector();");
            }
        });
        tournLoc.setOnAction(event -> {
            String locationName = tournLoc.getText();
            if (locationName != null && !locationName.trim().isEmpty()) {
                // Call the searchLocation function in the WebView
                webEngine.executeScript("searchLocation('" + locationName + "');");
            } else {
                showAlert("Error", "Please enter a location name.");
            }
        });

        // Handle error messages from JavaScript
        webEngine.setOnAlert(event -> {
            String message = event.getData();
            if (message.startsWith("Error:")) {
                showAlert("Error", message);
            }
        });


    }

    public class JavaConnector {
        public void sendLocation(String location) {
            Platform.runLater(() -> {
                System.out.println("Received location from JavaScript: " + location);
                tournLoc.setText(location);
            });
        }

    }



    private void loadMap() {
        WebEngine webEngine = map.getEngine();
        URL url = getClass().getResource("/map.html"); // Ensure this file is in src/main/resources
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.out.println("Error: map.html not found!");
        }
    }
    private void addTournAction() {
        if (!validateInputs()) return;

        try {
            // Create the tournament
            String name = tournName.getText();
            String location = tournLoc.getText();
            LocalDate startDate = tournStartDate.getValue();
            LocalDate endDate = tournEndDate.getValue();
            String sport = tournTOS.getValue();

            tournament currentTournament = new tournament(name, Date.valueOf(startDate), Date.valueOf(endDate), location, sport, selectedTeams.size());
            tournamentService.add(currentTournament);

            // Retrieve the ID of the newly created tournament
            int tournamentId = tournamentService.getLastAddedTournamentId();

            // Create results entries for each selected team
            for (team selectedTeam : selectedTeams) {
                results newResult = new results(tournamentId, selectedTeam.getTeamId());
                resultsService.add(newResult);
            }

            // Clear the selected teams list after the tournament is created
            selectedTeams.clear();
            selectedTeamsCount = 0;
            tournTOS.setDisable(false); // Re-enable sport selection if no teams exist

            showAlert("Success", "Tournament and team results added successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add tournament or results: " + e.getMessage());
        }
    }


    private void filterTeamsBySport() {
        teamList.clear();
        String selectedSport = tournTOS.getValue();

        if (selectedSport != null) {
            try {
                List<team> teams = teamService.returnListBySport(selectedSport);
                teamList.setAll(teams);  // Ensures all teams are added at once
                tabTeam.setItems(teamList); // Explicitly setting items again
                tabTeam.refresh();
            } catch (SQLException e) {
                showAlert("Error", "Failed to load teams: " + e.getMessage());
            }
        }
    }


    private void addActionButtonsToTable() {
        tabTeamActions.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button("Add");
            private final Button removeButton = new Button("Remove");
            private final HBox pane = new HBox(addButton, removeButton);

            {
                // Style the buttons
                addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                removeButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                // Add button logic
                addButton.setOnAction(event -> {
                    team selectedTeam = getTableView().getItems().get(getIndex());
                    addTeamToTournament(selectedTeam);
                    updateButtonsVisibility(selectedTeam); // Update button visibility
                });

                // Remove button logic
                removeButton.setOnAction(event -> {
                    team selectedTeam = getTableView().getItems().get(getIndex());
                    removeTeamFromTournament(selectedTeam);
                    updateButtonsVisibility(selectedTeam); // Update button visibility
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    team selectedTeam = getTableView().getItems().get(getIndex());
                    updateButtonsVisibility(selectedTeam); // Set visibility when updating the row
                    setGraphic(pane);
                }
            }

            private void updateButtonsVisibility(team selectedTeam) {
                boolean isAdded = selectedTeams.contains(selectedTeam);
                addButton.setVisible(!isAdded);
                removeButton.setVisible(isAdded);
            }
        });
    }


    private void addTeamToTournament(team selectedTeam) {
        if (!selectedTeams.contains(selectedTeam)) { // Avoid adding duplicates
            selectedTeams.add(selectedTeam);
            selectedTeamsCount++; // Increment the counter
            showAlert("Success", selectedTeam.getTeamName() + " added to tournament!");

            // Disable ComboBox when the first team is added
            tournTOS.setDisable(true);
        } else {
            showAlert("Warning", selectedTeam.getTeamName() + " is already added.");
        }

        tabTeam.refresh(); // Refresh to update button visibility
    }



    private void switchScreenConsult() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewTournament.fxml"));
            Parent root = loader.load();

            viewTournament controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("viewing tournaments");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) viewTournLabel.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open view tournament screen: " + e.getMessage());
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
            showAlert("Error", "Failed to open consult screen: " + e.getMessage());
        }
    }
    private boolean validateInputs() {
        String name = tournName.getText();
        String location = tournLoc.getText();
        LocalDate startDate = tournStartDate.getValue();
        LocalDate endDate = tournEndDate.getValue();
        String sport = tournTOS.getValue();

        // Check if tournament name contains only letters and spaces
        if (name == null || !name.matches("^[A-Za-z ]+$")) {
            showAlert("Validation Error", "Tournament name should only contain letters and spaces.");
            return false;
        }

        // Check if location is not null or empty
        if (location == null || location.trim().isEmpty()) {
            showAlert("Validation Error", "Tournament location cannot be empty.");
            return false;
        }

        // Check if start date is not before today
        LocalDate today = LocalDate.now();
        if (startDate == null || startDate.isBefore(today)) {
            showAlert("Validation Error", "Tournament start date cannot be before today's date.");
            return false;
        }

        // Check if end date is not before start date
        if (endDate == null || endDate.isBefore(startDate)) {
            showAlert("Validation Error", "Tournament end date cannot be before the start date.");
            return false;
        }

        // Check if type of sport is selected
        if (sport == null || sport.trim().isEmpty()) {
            showAlert("Validation Error", "Please select a type of sport.");
            return false;
        }

        return true;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void removeTeamFromTournament(team selectedTeam) {
        if (selectedTeams.contains(selectedTeam)) {
            selectedTeams.remove(selectedTeam);
            selectedTeamsCount--; // Decrement the counter
            showAlert("Success", selectedTeam.getTeamName() + " removed from tournament.");

            // Enable ComboBox if no teams are left
            if (selectedTeams.isEmpty()) {
                tournTOS.setDisable(false);
            }

            tabTeam.refresh(); // Refresh TableView to update button visibility
        }
    }


}
