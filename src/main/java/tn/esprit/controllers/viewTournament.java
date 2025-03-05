package tn.esprit.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class viewTournament {
    @FXML
    private TableView<team> tabTeam;

    @FXML
    private TableColumn<team, Void> tabTeamAction;

    @FXML
    private TableColumn<team, Integer> tabTeamId;

    @FXML
    private TableColumn<team, Integer> tabTeamL;

    @FXML
    private TableColumn<team, String> tabTeamName;

    @FXML
    private TableColumn<team, Integer> tabTeamNath;

    @FXML
    private TableColumn<team, String> tabTeamSport;

    @FXML
    private TableColumn<team, Integer> tabTeamW;

    @FXML
    private TableView<tournament> tabTourn;
    @FXML
    private TableColumn<tournament, Integer> tabTournid;
    @FXML
    private TableColumn<tournament, String> tabTournName;
    @FXML
    private TableColumn<tournament, String> tabTournLoc;
    @FXML
    private TableColumn<tournament, Date> tabTournSdate;
    @FXML
    private TableColumn<tournament, Date> tabTournEdate;
    @FXML
    private TableColumn<tournament, String> tabTournSport;
    @FXML
    private TableColumn<tournament, Void> tabTournActions;
    @FXML
    private TableColumn<tournament, String> tabTournWinner;
    @FXML
    private Label backBtn;
    @FXML
    private Label addTournLabelC;

    private final TournamentService tournamentService = new TournamentService();
    private final ObservableList<team> teamList = FXCollections.observableArrayList();
    private final resultsServices resultsService = new resultsServices();
    private final ObservableList<tournament> tournamentList = FXCollections.observableArrayList();
    private final teamServices teamService = new teamServices();

    @FXML
    public void initialize() {
        backBtn.setOnMouseClicked(event -> switchBackToCoachFront());
        addTournLabelC.setOnMouseClicked(event -> switchScreenAddTournament());

        tabTournid.setCellValueFactory(new PropertyValueFactory<>("tournamentId"));
        tabTournName.setCellValueFactory(new PropertyValueFactory<>("tournamentName"));
        tabTournLoc.setCellValueFactory(new PropertyValueFactory<>("tournamentLocation"));
        tabTournSdate.setCellValueFactory(new PropertyValueFactory<>("tournamentStartDate"));
        tabTournEdate.setCellValueFactory(new PropertyValueFactory<>("tournamentEndDate"));
        tabTournSport.setCellValueFactory(new PropertyValueFactory<>("tournamentTOS"));
        tabTournWinner.setCellValueFactory(cellData -> {
            int winnerId = cellData.getValue().getTournamentWinner();
            try {
                team winner = teamService.getTeamById(winnerId);
                if (winner != null) {
                    return new SimpleStringProperty(winner.getTeamName());
                }
            } catch (SQLException e) {
                System.out.println("Error fetching team by ID: " + e.getMessage());
            }
            return new SimpleStringProperty("N/A");
        });


        tabTourn.setItems(tournamentList);

        updatetournamentList();
        addActionButtonsToTable();
        tabTeamId.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        tabTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        tabTeamNath.setCellValueFactory(new PropertyValueFactory<>("teamNath"));
        tabTeamSport.setCellValueFactory(new PropertyValueFactory<>("teamTOS"));
        tabTeamW.setCellValueFactory(new PropertyValueFactory<>("teamW"));
        tabTeamL.setCellValueFactory(new PropertyValueFactory<>("teamL"));
        tabTeam.setItems(teamList);

        tabTourn.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadTeamsForTournament(newSelection.getTournamentId());
            }
        });
        tabTourn.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadTeamsForTournament(newSelection.getTournamentId());
                addWinnerButtons(newSelection);
            }
        });
    }
    private void loadTeamsForTournament(int tournamentId) {
        teamList.clear();
        try {
            List<results> resultsList = resultsService.returnList();
            List<Integer> teamIds = resultsList.stream()
                    .filter(result -> result.getTournamentId() == tournamentId)
                    .map(results::getTeamId)
                    .collect(Collectors.toList());

            List<team> teams = teamService.returnList().stream()
                    .filter(team -> teamIds.contains(team.getTeamId()))
                    .collect(Collectors.toList());

            teamList.addAll(teams);
            tabTeam.refresh();
        } catch (SQLException e) {
            showAlert("Error", "Failed to load teams: " + e.getMessage());
        }
    }


    void updatetournamentList() {
        tournamentList.clear();
        try {
            List<tournament> tournaments = tournamentService.returnList();
            tournamentList.addAll(tournaments);
            tabTourn.refresh();
        } catch (SQLException e) {
            showAlert("Error", "Failed to load tournament data: " + e.getMessage());
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
            showAlert("Error", "Failed to open Coach screen: " + e.getMessage());
        }
    }
    private void switchScreenAddTournament() {
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
            Stage currentStage = (Stage) addTournLabelC.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open add tournament screen: " + e.getMessage());
        }
    }
    private void openUpdateTournamentScreen(tournament tournament) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateTournament.fxml"));
            Parent root = loader.load();

            updateTournament controller = loader.getController();
            controller.initData(tournament);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Tournament");

            // Listen for window close and refresh the table
            stage.setOnHidden(event -> updatetournamentList());

            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to open update screen: " + e.getMessage());
        }
    }

    private void addActionButtonsToTable() {
        tabTournActions.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(updateButton, deleteButton);

            {
                updateButton.setStyle("-fx-background-color: #BCCCE0; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #D68C45; -fx-text-fill: white;");
                pane.setSpacing(5);

                // Update Button Action
                updateButton.setOnAction(event -> {
                    tournament tournament = (tournament) getTableView().getItems().get(getIndex());
                    openUpdateTournamentScreen(tournament);
                });

                // Delete Button Action
                deleteButton.setOnAction(event -> {
                    tournament tournament = (tournament) getTableView().getItems().get(getIndex());
                    deleteTournament(tournament);
                    System.out.println("Delete button clicked for team: " + tournament.getTournamentId());
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

    private void deleteTournament(tournament tournament) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this record?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    tournamentService.delete(tournament.getTournamentId());
                    tournamentList.remove(tournament);
                    showAlert("Success", "Tournament deleted successfully!");
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete tournament: " + e.getMessage());
                }
            }
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void declareWinner(tournament tournament, team winnerTeam) {
        try {
            // Update the tournament's winner
            tournament.setTournamentWinner(winnerTeam.getTeamId());
            tournamentService.update(tournament.getTournamentId(),tournament);

            // Increment winner's wins
            winnerTeam.setTeamW(winnerTeam.getTeamW() + 1);
            teamService.update(winnerTeam.getTeamId(), winnerTeam);

            // Increment losses for other teams in the tournament
            for (team t : teamList) {
                if (t.getTeamId() != winnerTeam.getTeamId()) {
                    t.setTeamL(t.getTeamL() + 1);
                    teamService.update(t.getTeamId(),t);
                }
            }

            updatetournamentList();
            loadTeamsForTournament(tournament.getTournamentId());
        } catch (SQLException e) {
            showAlert("Error", "Failed to declare winner: " + e.getMessage());
        }
    }
    private void addWinnerButtons(tournament tournament) {
        tabTeamAction.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    team team = getTableView().getItems().get(getIndex());
                    if (tournament.getTournamentWinner() != 0) {
                        setGraphic(new Label("Winner Already Declared"));
                    } else {
                        Button declareWinnerButton = new Button("Declare Winner");
                        declareWinnerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        declareWinnerButton.setOnAction(event -> declareWinner(tournament, team));
                        setGraphic(declareWinnerButton);
                    }
                }
            }
        });
    }




}
