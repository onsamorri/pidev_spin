package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.User;
import tn.esprit.entities.Injury;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.Severity;
import tn.esprit.services.InjuryServices;
import tn.esprit.services.UserServices;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddInjuryController {

    // Input fields for entering athlete information
    @FXML
    private TextField AthleteNameField;

    @FXML
    private TextField AthleteLastNameField;

    // ChoiceBox for selecting the type of injury
    @FXML
    private ChoiceBox<InjuryType> InjuryTypeBox;

    @FXML
    private TextField InjuryDescriptionField;

    // ChoiceBox for selecting injury severity
    @FXML
    private ChoiceBox<Severity> SeverityBox;

    // DatePicker for selecting the date of injury
    @FXML
    private DatePicker InjuryDatePicker;

    // Button to submit and add an injury record
    @FXML
    private Button addInjuryButton;

    @FXML
    public void initialize() {
        // Populate the severity dropdown with enum values
        SeverityBox.getItems().addAll(Severity.values());

        // Populate the injury type dropdown with enum values
        InjuryTypeBox.getItems().addAll(InjuryType.values());

        // Set default values for severity and injury type
        SeverityBox.setValue(Severity.MILD);
        InjuryTypeBox.setValue(InjuryType.STRAIN);

        // Set button action to call the addInjury() method when clicked
        addInjuryButton.setOnAction(event -> addInjury());

        // Restrict date picker to prevent selecting dates before the academic year
        setDatePickerConstraints();
    }

    private void setDatePickerConstraints() {
        // Get the current date
        LocalDate now = LocalDate.now();

        // Set the start of the academic year (September 1st)
        LocalDate startOfAcademicYear = LocalDate.of(now.getYear(), 9, 1);

        // If the current date is before September, set the academic year to the previous year
        if (now.isBefore(startOfAcademicYear)) {
            startOfAcademicYear = startOfAcademicYear.minusYears(1);
        }

        LocalDate finalStartOfAcademicYear = startOfAcademicYear;

        // Disable past dates before the academic year in the DatePicker
        // Set a custom factory for the DatePicker to control selectable dates
        InjuryDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Disable the date if:
                // 1. The cell is empty (no date available)
                // 2. The date is before the academic year start date (finalStartOfAcademicYear)
                setDisable(empty || item.isBefore(finalStartOfAcademicYear));
            }
        });

    }

    private void addInjury() {
        // Create service instances for managing injuries and users
        InjuryServices injuryService = new InjuryServices();
        UserServices userService = new UserServices();

        // Get input values from the form fields
        String user_fname = AthleteNameField.getText().trim();
        String user_lname = AthleteLastNameField.getText().trim();
        String injury_description = InjuryDescriptionField.getText().trim();
        Severity severity = SeverityBox.getValue();
        LocalDate injuryDate = InjuryDatePicker.getValue();
        InjuryType type = InjuryTypeBox.getValue();

        // Validate inputs: ensure all required fields are filled
        if (user_fname.isEmpty() || user_lname.isEmpty() || severity == null || injuryDate == null || type == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

        try (Connection con = MyDatabase.getInstance().getCon()) {
            // Fetch the athlete (User) by first and last name from the database
            User user = userService.getUserByName(con, user_fname, user_lname);

            // If the athlete is not found, show an error and stop the process
            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Athlete not found.");
                return;
            }

            // Create a new Injury object with the input values
            Injury injury = new Injury(user, type, injury_description, injuryDate, severity);

            // Save the injury record to the database
            injuryService.add(injury);

            // Show a success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Injury added successfully!");

            // Clear input fields after successful submission
            clearFields();
        } catch (SQLException e) {
            // Print the error and show an error alert if there’s a database issue
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add injury.");
        }
    }

    // Method to show alert messages
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to reset form fields after submission
    private void clearFields() {
        AthleteNameField.clear();
        AthleteLastNameField.clear();
        InjuryDescriptionField.clear();
        InjuryDatePicker.setValue(null);
        SeverityBox.setValue(Severity.MODERATE); // Reset to default severity
        InjuryTypeBox.setValue(InjuryType.BRUISE); // Reset to default injury type
    }
}
