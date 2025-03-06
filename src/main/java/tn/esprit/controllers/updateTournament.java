package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import tn.esprit.entities.results;
import tn.esprit.entities.team;
import tn.esprit.entities.tournament;
import tn.esprit.services.TournamentService;
import tn.esprit.services.resultsServices;
import tn.esprit.services.teamServices;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class updateTournament {
    @FXML
    private WebView map;
    @FXML
    private Label viewTournLabel, backBtn;
    @FXML
    private Button updateTournbtn;
    @FXML
    private TextField tournLoc, tournName;
    @FXML
    private DatePicker tournEndDate, tournStartDate;
    @FXML
    private ComboBox<String> tournTOS;
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

    private final TournamentService tournamentService = new TournamentService();
    private final teamServices teamService = new teamServices();
    private final ObservableList<team> teamList = FXCollections.observableArrayList();
    private final ObservableList<team> selectedTeams = FXCollections.observableArrayList();
    private tournament selectedTournament;
    private JavaConnector javaConnector;

    public void initData(tournament tournament) {
        this.selectedTournament = tournament;
        tournName.setText(tournament.getTournamentName());
        tournStartDate.setValue(tournament.getTournamentStartDate().toLocalDate());
        tournEndDate.setValue(tournament.getTournamentEndDate().toLocalDate());
        tournLoc.setText(tournament.getTournamentLocation());
        tournTOS.setValue(tournament.getTournamentTOS());
        updateTournbtn.setOnAction(event -> updateTournamentAction());
        loadMap();
        initializeTable();

        // Load teams already associated with the tournament
        try {
            List<team> teamsInTournament = teamService.getTeamsByTournamentId(tournament.getTournamentId());
            selectedTeams.setAll(teamsInTournament); // Initialize selectedTeams with teams already in the tournament
            System.out.println("Initial Selected Teams: " + selectedTeams); // Debugging

            // Disable the ComboBox if teams are already assigned
            if (!selectedTeams.isEmpty()) {
                tournTOS.setDisable(true);
                System.out.println("ComboBox disabled on initialization."); // Debugging
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load teams for the tournament: " + e.getMessage());
        }

        filterTeamsBySport();

        // Add listener to ComboBox to update TableView when sport changes
        tournTOS.setOnAction(event -> filterTeamsBySport());

        // Add listener to tournLoc TextField to search location
        tournLoc.setOnAction(event -> {
            String locationName = tournLoc.getText();
            if (locationName != null && !locationName.trim().isEmpty()) {
                WebEngine webEngine = map.getEngine();
                webEngine.executeScript("searchLocation('" + locationName + "');");
            } else {
                showAlert("Error", "Please enter a location name.");
            }
        });
    }

    private void loadMap() {
        WebEngine webEngine = map.getEngine();
        webEngine.load(getClass().getResource("/map.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                javaConnector = new JavaConnector();
                window.setMember("javaConnector", javaConnector);

                // Handle errors from JavaScript
                webEngine.setOnAlert(event -> {
                    String message = event.getData();
                    if (message.startsWith("Error:")) {
                        showAlert("Error", message);
                    }
                });
            }
        });
    }

    public class JavaConnector {
        public void sendLocation(String location) {
            Platform.runLater(() -> tournLoc.setText(location));
        }
    }

    private void initializeTable() {
        tabTeamId.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        tabTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        tabTeamNath.setCellValueFactory(new PropertyValueFactory<>("teamNath"));
        tabTeamWins.setCellValueFactory(new PropertyValueFactory<>("teamW"));
        tabTeamLosses.setCellValueFactory(new PropertyValueFactory<>("teamL"));
        tabTeamSport.setCellValueFactory(new PropertyValueFactory<>("teamTOS"));
        tabTeam.setItems(teamList);
        addActionButtonsToTable();

        // Refresh the TableView to ensure button visibility is updated
        tabTeam.refresh();
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
                    updateButtonsVisibility(selectedTeam); // Update button visibility for this row
                    setGraphic(pane);
                }
            }

            private void updateButtonsVisibility(team selectedTeam) {
                boolean isAdded = selectedTeams.contains(selectedTeam);
                addButton.setVisible(!isAdded);
                removeButton.setVisible(isAdded);
                System.out.println("Team: " + selectedTeam.getTeamName() + ", isAdded: " + isAdded); // Debugging
            }
        });
    }
    private void addTeamToTournament(team selectedTeam) {
        if (!selectedTeams.contains(selectedTeam)) {
            selectedTeams.add(selectedTeam);
            showAlert("Success", selectedTeam.getTeamName() + " added to tournament!");

            // Disable the ComboBox when the first team is added
            if (selectedTeams.size() == 1) {
                tournTOS.setDisable(true);
            }

            tabTeam.refresh(); // Refresh TableView to update button visibility
        } else {
            showAlert("Warning", selectedTeam.getTeamName() + " is already added.");
        }
    }

    private void removeTeamFromTournament(team selectedTeam) {
        if (selectedTeams.contains(selectedTeam)) {
            selectedTeams.remove(selectedTeam);
            showAlert("Success", selectedTeam.getTeamName() + " removed from tournament.");

            // Enable the ComboBox if no teams are left
            if (selectedTeams.isEmpty()) {
                tournTOS.setDisable(false);
            }

            tabTeam.refresh(); // Refresh TableView to update button visibility
        }
    }

    private void filterTeamsBySport() {
        teamList.clear();
        String selectedSport = tournTOS.getValue();

        if (selectedSport != null) {
            try {
                List<team> teams = teamService.returnListBySport(selectedSport);
                teamList.setAll(teams);
                System.out.println("Filtered Teams: " + teamList); // Debugging

                // Refresh the TableView to update button visibility
                tabTeam.setItems(teamList);
                tabTeam.refresh();
            } catch (SQLException e) {
                showAlert("Error", "Failed to load teams: " + e.getMessage());
            }
        }
    }
    @FXML
    private void updateTournamentAction() {
        if (!validateInputs()) return;

        // Update tournament details
        selectedTournament.setTournamentName(tournName.getText());
        selectedTournament.setTournamentStartDate(java.sql.Date.valueOf(tournStartDate.getValue()));
        selectedTournament.setTournamentEndDate(java.sql.Date.valueOf(tournEndDate.getValue()));
        selectedTournament.setTournamentLocation(tournLoc.getText());
        selectedTournament.setTournamentTOS(tournTOS.getValue());
        selectedTournament.setTournamentNbteams(selectedTeams.size());

        try {
            // Start a transaction
            tournamentService.beginTransaction();

            // Update the tournament in the database
            tournamentService.update(selectedTournament.getTournamentId(), selectedTournament);

            // Update team associations
            resultsServices resultsService = new resultsServices();

            // Remove all existing team associations for this tournament
            resultsService.deleteByTournamentId(selectedTournament.getTournamentId());

            // Add new team associations
            for (team selectedTeam : selectedTeams) {
                results newResult = new results(selectedTournament.getTournamentId(), selectedTeam.getTeamId());
                resultsService.add(newResult);
            }

            // Commit the transaction
            tournamentService.commitTransaction();

            showAlert("Success", "Tournament and team associations updated successfully!");
            updateTournbtn.getScene().getWindow().hide();
        } catch (SQLException e) {
            // Roll back the transaction in case of error
            tournamentService.rollbackTransaction();
            showAlert("Error", "Failed to update tournament or team associations: " + e.getMessage());
        }
    }
    private boolean validateInputs() {
        if (tournName.getText().isEmpty() || tournLoc.getText().isEmpty() || tournTOS.getValue() == null || tournStartDate.getValue() == null || tournEndDate.getValue() == null) {
            showAlert("Validation Error", "All fields must be filled out.");
            return false;
        }
        if (tournStartDate.getValue().isBefore(LocalDate.now())) {
            showAlert("Validation Error", "Start date cannot be before today.");
            return false;
        }
        if (tournEndDate.getValue().isBefore(tournStartDate.getValue())) {
            showAlert("Validation Error", "End date cannot be before start date.");
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
}