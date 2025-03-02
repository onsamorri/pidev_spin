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
import tn.esprit.entities.team;
import tn.esprit.entities.tournament;
import tn.esprit.services.TournamentService;
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
    }

    private void addActionButtonsToTable() {
        tabTeamActions.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button("Add");
            private final Button removeButton = new Button("Remove");
            private final HBox pane = new HBox(addButton, removeButton);

            {
                addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                removeButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                addButton.setOnAction(event -> {
                    team selectedTeam = getTableView().getItems().get(getIndex());
                    addTeamToTournament(selectedTeam);
                    updateButtonsVisibility(selectedTeam);
                });

                removeButton.setOnAction(event -> {
                    team selectedTeam = getTableView().getItems().get(getIndex());
                    removeTeamFromTournament(selectedTeam);
                    updateButtonsVisibility(selectedTeam);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    team selectedTeam = getTableView().getItems().get(getIndex());
                    updateButtonsVisibility(selectedTeam);
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
        if (!selectedTeams.contains(selectedTeam)) {
            selectedTeams.add(selectedTeam);
            showAlert("Success", selectedTeam.getTeamName() + " added to tournament!");
            tournTOS.setDisable(true);
        } else {
            showAlert("Warning", selectedTeam.getTeamName() + " is already added.");
        }
        tabTeam.refresh();
    }

    private void removeTeamFromTournament(team selectedTeam) {
        if (selectedTeams.contains(selectedTeam)) {
            selectedTeams.remove(selectedTeam);
            showAlert("Success", selectedTeam.getTeamName() + " removed from tournament.");
            if (selectedTeams.isEmpty()) {
                tournTOS.setDisable(false);
            }
            tabTeam.refresh();
        }
    }

    private void filterTeamsBySport() {
        teamList.clear();
        String selectedSport = tournTOS.getValue();

        if (selectedSport != null) {
            try {
                List<team> teams = teamService.returnListBySport(selectedSport);
                teamList.setAll(teams);
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
        selectedTournament.setTournamentName(tournName.getText());
        selectedTournament.setTournamentStartDate(java.sql.Date.valueOf(tournStartDate.getValue()));
        selectedTournament.setTournamentEndDate(java.sql.Date.valueOf(tournEndDate.getValue()));
        selectedTournament.setTournamentLocation(tournLoc.getText());
        selectedTournament.setTournamentTOS(tournTOS.getValue());
        try {
            tournamentService.update(selectedTournament.getTournamentId(), selectedTournament);
            updateTournbtn.getScene().getWindow().hide();
        } catch (SQLException e) {
            showAlert("Error", "Failed to update tournament: " + e.getMessage());
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