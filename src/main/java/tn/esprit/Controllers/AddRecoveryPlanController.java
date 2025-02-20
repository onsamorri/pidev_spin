package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.*;
import tn.esprit.services.RecoveryPlanServices;
import tn.esprit.services.InjuryServices;
import tn.esprit.services.UserServices;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddRecoveryPlanController {

    @FXML
    private TextField AthleteNameField;

    @FXML
    private TextField AthleteLastNameField;

    @FXML
    private ChoiceBox<RecoveryGoal> RecoveryGoalBox;

    @FXML
    private TextField RecoveryDescriptionField;

    @FXML
    private DatePicker RecoveryStartDatePicker;

    @FXML
    private DatePicker RecoveryEndDatePicker;

    @FXML
    private ChoiceBox<RecoveryStatus> RecoveryStatusBox;

    @FXML
    private Button addRecoveryPlanButton;

    @FXML
    public void initialize() {
        RecoveryGoalBox.getItems().addAll(RecoveryGoal.values());
        RecoveryStatusBox.getItems().addAll(RecoveryStatus.values());
        RecoveryStatusBox.setValue(RecoveryStatus.PENDING);
        RecoveryGoalBox.setValue(RecoveryGoal.PREVENTION);
        addRecoveryPlanButton.setOnAction(event -> addRecoveryPlan());
        setDatePickerConstraints();
    }

    private void setDatePickerConstraints() {
        LocalDate now = LocalDate.now();
        LocalDate startOfAcademicYear = LocalDate.of(now.getYear(), 9, 1);
        if (now.isBefore(startOfAcademicYear)) {
            startOfAcademicYear = startOfAcademicYear.minusYears(1);
        }
        LocalDate finalStartOfAcademicYear = startOfAcademicYear;
        RecoveryStartDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(finalStartOfAcademicYear));
            }
        });
    }

    private void addRecoveryPlan() {
        RecoveryPlanServices recoveryPlanServices = new RecoveryPlanServices();
        InjuryServices injuryService = new InjuryServices();
        UserServices userService = new UserServices();

        String user_fname = AthleteNameField.getText();
        String user_lname = AthleteLastNameField.getText();
        String recovery_Description = RecoveryDescriptionField.getText();
        RecoveryGoal goal = RecoveryGoalBox.getValue();
        LocalDate recovery_StartDate = RecoveryStartDatePicker.getValue();
        LocalDate 	recovery_EndDate = RecoveryEndDatePicker.getValue();
        RecoveryStatus Recovery_Status = RecoveryStatusBox.getValue();

        if (user_fname.isEmpty() || user_lname.isEmpty() || goal == null || 	recovery_StartDate== null || recovery_EndDate == null || Recovery_Status == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

        try (Connection con = MyDatabase.getInstance().getCon()) {
            User user = userService.getUserByName(con, user_fname, user_lname);

            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Athlete not found.");
                return;
            }

            // Fetch the Injury object based on the user's injury records
            Injury injury = injuryService.findByUserId(user.getUser_id());

            if (injury == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No injury record found for this athlete.");
                return;
            }

            // Create the RecoveryPlan object
            RecoveryPlan recoveryPlan = new RecoveryPlan(injury, user, goal, recovery_Description, recovery_StartDate, recovery_EndDate, Recovery_Status);

            // Add the recovery plan
            recoveryPlanServices.add(recoveryPlan);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Recovery plan added successfully!");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add recovery plan.");
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        AthleteNameField.clear();
        AthleteLastNameField.clear();
        RecoveryDescriptionField.clear();
        RecoveryStartDatePicker.setValue(null);
        RecoveryEndDatePicker.setValue(null);
        RecoveryGoalBox.setValue(RecoveryGoal.PREVENTION);
        RecoveryStatusBox.setValue(RecoveryStatus.PENDING);
    }
}
