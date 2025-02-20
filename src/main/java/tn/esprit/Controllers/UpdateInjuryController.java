package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Injury;
import tn.esprit.entities.User;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.Severity;
import tn.esprit.services.InjuryServices;

import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateInjuryController {

    @FXML
    private TextField AthleteNameField;

    @FXML
    private TextField AthleteLastNameField;

    @FXML
    private ChoiceBox<InjuryType> InjuryTypeBox;

    @FXML
    private TextField InjuryDescriptionField;

    @FXML
    private DatePicker InjuryDatePicker;

    @FXML
    private ChoiceBox<Severity> SeverityBox;

    @FXML
    private Button updateInjuryButton;

    @FXML
    private Button cancelButton;

    private InjuryServices InjuryService;
    private Injury selectedInjury; // To hold the injury being updated
    private ShowInjuryController showInjuryController; // Reference to the ShowInjuryController

    public void setInjuryData(Injury selectedInjury) {
        this.selectedInjury = selectedInjury; // Pass the selected Injury when opening this controller
        // Fill fields with the selected Injury's data
        if (selectedInjury != null) {
            AthleteNameField.setText(selectedInjury.getUser().getUser_fname());
            AthleteLastNameField.setText(selectedInjury.getUser().getUser_lname());
            InjuryDescriptionField.setText(selectedInjury.getInjury_description());
            InjuryTypeBox.setValue(selectedInjury.getInjuryType());
            InjuryDatePicker.setValue(selectedInjury.getInjuryDate());
            SeverityBox.setValue(selectedInjury.getInjury_severity());
        }
    }

    public void setShowInjuryController(ShowInjuryController showInjuryController) {
        this.showInjuryController = showInjuryController; // Set the reference to ShowInjuryController
    }

    @FXML
    public void initialize() {
        InjuryService = new InjuryServices();

        // Populate ChoiceBoxes with enum values
        InjuryTypeBox.getItems().addAll(InjuryType.values());
        SeverityBox.getItems().addAll(Severity.values());

        // Set default values (optional)
        InjuryTypeBox.setValue(InjuryType.STRAIN);
        SeverityBox.setValue(Severity.MILD);

        // Set button actions
        updateInjuryButton.setOnAction(event -> updateInjury());
        cancelButton.setOnAction(event -> closeWindow());

        // Set the date picker to not allow dates before September 1st of the current academic year
        setDatePickerConstraints();
    }

    private void setDatePickerConstraints() {
        LocalDate now = LocalDate.now();
        LocalDate startOfAcademicYear = LocalDate.of(now.getYear(), 9, 1);

        // If the current date is before September 1st, set the start date to September 1st of the previous year
        if (now.isBefore(startOfAcademicYear)) {
            startOfAcademicYear = startOfAcademicYear.minusYears(1);
        }

        LocalDate finalStartOfAcademicYear = startOfAcademicYear;
        InjuryDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(finalStartOfAcademicYear));
            }
        });
    }

    private void updateInjury() {
        String user_fname = AthleteNameField.getText();
        String user_lname = AthleteLastNameField.getText();
        String injury_description = InjuryDescriptionField.getText();
        InjuryType type = InjuryTypeBox.getValue();
        LocalDate InjuryDate = InjuryDatePicker.getValue();
        Severity severity = SeverityBox.getValue();

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

        if (type == null) {
            InjuryTypeBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            InjuryTypeBox.getStyleClass().remove("invalid-input");
        }

        if (InjuryDate == null) {
            InjuryDatePicker.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            InjuryDatePicker.getStyleClass().remove("invalid-input");
        }

        if (severity == null) {
            SeverityBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            SeverityBox.getStyleClass().remove("invalid-input");
        }

        if (!valid) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

        // Update the User associated with the selected Injury
        User user = selectedInjury.getUser();
        if (user != null) {
            user.setUser_fname(user_fname); // Update the first name
            user.setUser_lname(user_lname); // Update the last name
        }

        // Update the selected Injury with the new data
        selectedInjury.setInjuryType(type);
        selectedInjury.setInjury_description(injury_description);
        selectedInjury.setInjuryDate(InjuryDate);
        selectedInjury.setInjury_severity(severity);

        try {
            // Update Injury in the database
            InjuryService.update(selectedInjury);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Injury updated successfully!");
            showInjuryController.refreshTable(); // Refresh the table in ShowInjuryController
            closeWindow(); // Close the window after update
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update Injury.");
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

    private void clearFields() {
        AthleteNameField.clear();
        AthleteLastNameField.clear();
        InjuryDatePicker.setValue(null);
        InjuryTypeBox.setValue(InjuryType.DISLOCATION);
        InjuryDescriptionField.clear();
        SeverityBox.setValue(Severity.SEVERE);

        // Remove invalid-input class
        AthleteNameField.getStyleClass().remove("invalid-input");
        AthleteLastNameField.getStyleClass().remove("invalid-input");
        InjuryTypeBox.getStyleClass().remove("invalid-input");
        InjuryDescriptionField.getStyleClass().remove("invalid-input");
        InjuryDatePicker.getStyleClass().remove("invalid-input");
        SeverityBox.getStyleClass().remove("invalid-input");
    }
}
