package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.RecoveryPlan;
import tn.esprit.entities.Injury;
import tn.esprit.entities.User;
import tn.esprit.entities.RecoveryGoal;
import tn.esprit.entities.RecoveryStatus;
import tn.esprit.services.RecoveryPlanServices;

import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateRecoveryPlanController {

    @FXML
    private TextField AthleteNameField;

    @FXML
    private TextField AthleteLastNameField;

    @FXML
    private TextField RecoveryDescriptionField;

    @FXML
    private DatePicker RecoveryStartDatePicker;

    @FXML
    private DatePicker RecoveryEndDatePicker;

    @FXML
    private ChoiceBox<RecoveryGoal> RecoveryGoalBox;

    @FXML
    private ChoiceBox<RecoveryStatus> RecoveryStatusBox;

    @FXML
    private Button updateRecoveryPlanButton;

    @FXML
    private Button cancelButton;

    private RecoveryPlanServices recoveryPlanService;
    private RecoveryPlan selectedRecoveryPlan; // To hold the recovery plan being updated
    private ShowRecoveryPlanController showRecoveryPlanController; // Reference to the ShowRecoveryPlanController

    public void setRecoveryPlanData(RecoveryPlan selectedRecoveryPlan) {
        this.selectedRecoveryPlan = selectedRecoveryPlan; // Pass the selected RecoveryPlan when opening this controller
        // Fill fields with the selected RecoveryPlan's data
        if (selectedRecoveryPlan != null) {
            AthleteNameField.setText(selectedRecoveryPlan.getUser().getUser_fname());
            AthleteLastNameField.setText(selectedRecoveryPlan.getUser().getUser_lname());
            RecoveryDescriptionField.setText(selectedRecoveryPlan.getRecovery_Description());
            RecoveryGoalBox.setValue(selectedRecoveryPlan.getRecovery_Goal());
            RecoveryStartDatePicker.setValue(selectedRecoveryPlan.getRecovery_StartDate());
            RecoveryEndDatePicker.setValue(selectedRecoveryPlan.getRecovery_EndDate());
            RecoveryStatusBox.setValue(selectedRecoveryPlan.getRecovery_Status());
        }
    }

    public void setShowRecoveryPlanController(ShowRecoveryPlanController showRecoveryPlanController) {
        this.showRecoveryPlanController = showRecoveryPlanController; // Set the reference to ShowRecoveryPlanController
    }

    @FXML
    public void initialize() {
        recoveryPlanService = new RecoveryPlanServices();

        // Populate ChoiceBoxes with enum values
        RecoveryGoalBox.getItems().addAll(RecoveryGoal.values());
        RecoveryStatusBox.getItems().addAll(RecoveryStatus.values());

        // Set button actions
        updateRecoveryPlanButton.setOnAction(event -> updateRecoveryPlan());
        cancelButton.setOnAction(event -> closeWindow());
    }

    private void updateRecoveryPlan() {
        String user_fname = AthleteNameField.getText();
        String user_lname = AthleteLastNameField.getText();
        String recovery_description = RecoveryDescriptionField.getText();
        RecoveryGoal goal = RecoveryGoalBox.getValue();
        LocalDate startDate = RecoveryStartDatePicker.getValue();
        LocalDate endDate = RecoveryEndDatePicker.getValue();
        RecoveryStatus status = RecoveryStatusBox.getValue();

        // Validate inputs
        boolean valid = true;

        if (user_fname.isEmpty()) {
            AthleteNameField.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            AthleteNameField.getStyleClass().remove("invalid-input");
        }

        if (user_lname.isEmpty()) {
            AthleteLastNameField.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            AthleteLastNameField.getStyleClass().remove("invalid-input");
        }

        if (goal == null) {
            RecoveryGoalBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            RecoveryGoalBox.getStyleClass().remove("invalid-input");
        }

        if (startDate == null) {
            RecoveryStartDatePicker.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            RecoveryStartDatePicker.getStyleClass().remove("invalid-input");
        }

        if (endDate == null) {
            RecoveryEndDatePicker.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            RecoveryEndDatePicker.getStyleClass().remove("invalid-input");
        }

        if (status == null) {
            RecoveryStatusBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            RecoveryStatusBox.getStyleClass().remove("invalid-input");
        }

        if (!valid) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

        // Update the User associated with the selected RecoveryPlan
        User user = selectedRecoveryPlan.getUser();
        if (user != null) {
            user.setUser_fname(user_fname); // Update the first name
            user.setUser_lname(user_lname); // Update the last name
        }

        // Update the selected RecoveryPlan with the new data
        selectedRecoveryPlan.setRecovery_Goal(goal);
        selectedRecoveryPlan.setRecovery_Description(recovery_description);
        selectedRecoveryPlan.setRecovery_StartDate(startDate);
        selectedRecoveryPlan.setRecovery_EndDate(endDate);
        selectedRecoveryPlan.setRecovery_Status(status);

        try {
            // Update RecoveryPlan in the database
            recoveryPlanService.update(selectedRecoveryPlan);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Recovery Plan updated successfully!");
            showRecoveryPlanController.refreshTable(); // Refresh the table in ShowRecoveryPlanController
            closeWindow(); // Close the window after update
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update Recovery Plan.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
