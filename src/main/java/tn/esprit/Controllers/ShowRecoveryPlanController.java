package tn.esprit.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.entities.RecoveryPlan;
import tn.esprit.services.RecoveryPlanServices;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowRecoveryPlanController {

    @FXML
    private TableView<RecoveryPlan> RecoveryTable;

    @FXML
    private TableColumn<RecoveryPlan, String> RecoveryIdColumn;

    @FXML
    private TableColumn<RecoveryPlan, String> AthleteFirstNameColumn;

    @FXML
    private TableColumn<RecoveryPlan, String> AthleteLastNameColumn;

    @FXML
    private TableColumn<RecoveryPlan, String> RecoveryGoalColumn;

    @FXML
    private TableColumn<RecoveryPlan, String> RecoveryDescriptionColumn;

    @FXML
    private TableColumn<RecoveryPlan, String> RecoveryStartDateColumn;

    @FXML
    private TableColumn<RecoveryPlan, String> RecoveryEndDateColumn;

    @FXML
    private TableColumn<RecoveryPlan, String> RecoveryStatusColumn;

    @FXML
    private TableColumn<RecoveryPlan, Void> actionsColumn;

    private final RecoveryPlanServices recoveryPlanServices = new RecoveryPlanServices();
    private ObservableList<RecoveryPlan> recoveryPlanList;

    @FXML
    public void initialize() {
        recoveryPlanList = FXCollections.observableArrayList();
        loadRecoveryPlans();

        // Bind columns to RecoveryPlan attributes
        RecoveryIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRecovery_id())));

        AthleteFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getUser_fname() : "Unknown"));

        AthleteLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser() != null ? cellData.getValue().getUser().getUser_lname() : "Unknown"));

        RecoveryGoalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_Goal() != null ? cellData.getValue().getRecovery_Goal().toString() : "Unknown"));

        RecoveryDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_Description()));

        RecoveryStartDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_StartDate() != null ? cellData.getValue().getRecovery_StartDate().toString() : "Unknown"));

        RecoveryEndDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_EndDate() != null ? cellData.getValue().getRecovery_EndDate().toString() : "Unknown"));

        RecoveryStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_Status() != null ? cellData.getValue().getRecovery_Status().toString() : "Unknown"));

        // Initialize actions column
        actionsColumn.setCellFactory(createButtonCellFactory());

        RecoveryTable.setItems(recoveryPlanList);
    }

    private void loadRecoveryPlans() {
        try {
            List<RecoveryPlan> recoveryPlans = recoveryPlanServices.getAll();
            recoveryPlanList.setAll(recoveryPlans);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load recovery plans.");
        }
    }

    private Callback<TableColumn<RecoveryPlan, Void>, TableCell<RecoveryPlan, Void>> createButtonCellFactory() {
        return param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                updateButton.setOnAction(event -> {
                    RecoveryPlan selectedRecoveryPlan = getTableView().getItems().get(getIndex());
                    updateRecoveryPlan(selectedRecoveryPlan);
                });

                deleteButton.setOnAction(event -> {
                    RecoveryPlan selectedRecoveryPlan = getTableView().getItems().get(getIndex());
                    deleteRecoveryPlan(selectedRecoveryPlan);
                });

                HBox hBox = new HBox(updateButton, deleteButton);
                hBox.setSpacing(10);
                setGraphic(hBox);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(getGraphic());
                }
            }
        };
    }

    private void updateRecoveryPlan(RecoveryPlan selectedRecoveryPlan) {
        if (selectedRecoveryPlan != null) {
            openUpdateRecoveryPlanInterface(selectedRecoveryPlan);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a recovery plan to update.");
        }
    }

    private void openUpdateRecoveryPlanInterface(RecoveryPlan selectedRecoveryPlan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateRecoveryPlan.fxml"));
            Parent updateRecoveryPlanRoot = loader.load();

            UpdateRecoveryPlanController updateRecoveryPlanController = loader.getController();
            updateRecoveryPlanController.setRecoveryPlanData(selectedRecoveryPlan);
            updateRecoveryPlanController.setShowRecoveryPlanController(this);

            Stage updateRecoveryPlanStage = new Stage();
            updateRecoveryPlanStage.setTitle("Update Recovery Plan");
            updateRecoveryPlanStage.setScene(new Scene(updateRecoveryPlanRoot));
            updateRecoveryPlanStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Update Recovery Plan interface.");
        }
    }

    private void deleteRecoveryPlan(RecoveryPlan selectedRecoveryPlan) {
        if (selectedRecoveryPlan != null) {
            new DeleteRecoveryPlanController().deleteRecoveryPlan(selectedRecoveryPlan);
            loadRecoveryPlans();
            RecoveryTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a recovery plan to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void refreshTable() {
        loadRecoveryPlans();
        RecoveryTable.refresh();
    }
}
