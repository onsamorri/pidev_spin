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

    // UI components from the FXML file
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

    // Services and selected injury reference
    private InjuryServices InjuryService;
    private Injury selectedInjury; // Holds the injury being updated
    private ShowInjuryController showInjuryController; // Reference to another controller

    /**
      Sets the data of the selected injury into the form fields*/
    public void setInjuryData(Injury selectedInjury) {
        this.selectedInjury = selectedInjury;
        // Populate the form with injury data if available
        if (selectedInjury != null) {
            AthleteNameField.setText(selectedInjury.getUser().getUser_fname());
            // Set the athlete's first name field with the first name from the selected injury's user
            AthleteLastNameField.setText(selectedInjury.getUser().getUser_lname());
            InjuryDescriptionField.setText(selectedInjury.getInjury_description());
            InjuryTypeBox.setValue(selectedInjury.getInjuryType());
            InjuryDatePicker.setValue(selectedInjury.getInjuryDate());
            SeverityBox.setValue(selectedInjury.getInjury_severity());
        }
    }

    /**
     * Sets a reference to the ShowInjuryController to refresh data after update
     */
    public void setShowInjuryController(ShowInjuryController showInjuryController) {
        this.showInjuryController = showInjuryController;
    }

    /**
     * Initializes the controller and sets up event handlers
     */
    @FXML
    public void initialize() {
        InjuryService = new InjuryServices();

        // Populate ChoiceBoxes with enum values
        InjuryTypeBox.getItems().addAll(InjuryType.values());
        SeverityBox.getItems().addAll(Severity.values());

        // Set default values
        InjuryTypeBox.setValue(InjuryType.STRAIN);
        SeverityBox.setValue(Severity.MILD);

        // Attach button click events
        updateInjuryButton.setOnAction(event -> updateInjury());
        cancelButton.setOnAction(event -> closeWindow());

        // Set restrictions on the date picker
        setDatePickerConstraints();
    }

    /**
     * Restricts the DatePicker to prevent selecting past dates before the academic year
     */
    private void setDatePickerConstraints() {
        LocalDate now = LocalDate.now();
        LocalDate startOfAcademicYear = LocalDate.of(now.getYear(), 9, 1);

        // If current date is before September 1st, adjust to previous year
        if (now.isBefore(startOfAcademicYear)) {
            startOfAcademicYear = startOfAcademicYear.minusYears(1);
        }

        LocalDate finalStartOfAcademicYear = startOfAcademicYear;
        InjuryDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Disable dates before the start of the academic year
                setDisable(empty || item.isBefore(finalStartOfAcademicYear));
            }
        });
    }

    /**
     * Handles updating an existing injury record
     */
    private void updateInjury() {
        String user_fname = AthleteNameField.getText();
        String user_lname = AthleteLastNameField.getText();
        String injury_description = InjuryDescriptionField.getText();
        InjuryType type = InjuryTypeBox.getValue();
        LocalDate InjuryDate = InjuryDatePicker.getValue();
        Severity severity = SeverityBox.getValue();

        // Input validation
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

        // Update the user's information
        User user = selectedInjury.getUser();
        if (user != null) {
            user.setUser_fname(user_fname);
            user.setUser_lname(user_lname);
        }

        // Update injury details
        selectedInjury.setInjuryType(type);
        selectedInjury.setInjury_description(injury_description);
        selectedInjury.setInjuryDate(InjuryDate);
        selectedInjury.setInjury_severity(severity);

        try {
            // Update the injury in the database
            InjuryService.update(selectedInjury);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Injury updated successfully!");
            showInjuryController.refreshTable(); // Refresh the table
            closeWindow(); // Close the window
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update Injury.");
        }
    }

    /**
     * Closes the update injury window
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert message
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
