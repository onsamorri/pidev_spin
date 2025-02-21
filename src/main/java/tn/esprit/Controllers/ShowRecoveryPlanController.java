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
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    private final RecoveryPlanServices recoveryPlanServices = new RecoveryPlanServices();
    private ObservableList<RecoveryPlan> recoveryPlanList;

    @FXML
    public void initialize() {
        recoveryPlanList = FXCollections.observableArrayList();
        loadRecoveryPlans();

        RecoveryIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRecovery_id())));
        AthleteFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getUser_fname()));
        AthleteLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getUser_lname()));
        RecoveryGoalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_Goal().toString()));
        RecoveryDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_Description()));
        RecoveryStartDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_StartDate().toString()));
        RecoveryEndDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_EndDate().toString()));
        RecoveryStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRecovery_Status().toString()));

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

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddRecoveryPlan.fxml"));
            Parent addRecoveryPlanRoot = loader.load();
            Stage addRecoveryPlanStage = new Stage();
            addRecoveryPlanStage.setTitle("Add Recovery Plan");
            addRecoveryPlanStage.setScene(new Scene(addRecoveryPlanRoot));
            addRecoveryPlanStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Add Recovery Plan interface.");
        }
    }

    @FXML
    private void handleUpdate() {
        RecoveryPlan selectedRecoveryPlan = RecoveryTable.getSelectionModel().getSelectedItem();
        if (selectedRecoveryPlan != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateRecoveryPlan.fxml"));
                Parent updateRecoveryPlanRoot = loader.load();
                UpdateRecoveryPlanController controller = loader.getController();
                controller.setRecoveryPlanData(selectedRecoveryPlan);
                Stage updateRecoveryPlanStage = new Stage();
                updateRecoveryPlanStage.setTitle("Update Recovery Plan");
                updateRecoveryPlanStage.setScene(new Scene(updateRecoveryPlanRoot));
                updateRecoveryPlanStage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Update Recovery Plan interface.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a recovery plan to update.");
        }
    }

    @FXML
    private void handleDelete() {
        RecoveryPlan selectedRecoveryPlan = RecoveryTable.getSelectionModel().getSelectedItem();
        if (selectedRecoveryPlan != null) {
            try {
                recoveryPlanServices.delete(selectedRecoveryPlan);
                loadRecoveryPlans();
                RecoveryTable.refresh();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete recovery plan.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a recovery plan to delete.");
        }
    }

    public void refreshTable() {
        loadRecoveryPlans(); // Reload data from the database
        RecoveryTable.refresh(); // Refresh the table view
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
