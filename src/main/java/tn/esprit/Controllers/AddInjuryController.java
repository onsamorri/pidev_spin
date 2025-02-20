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

    @FXML
    private TextField AthleteNameField;

    @FXML
    private TextField AthleteLastNameField;


    @FXML
    private ChoiceBox<InjuryType> InjuryTypeBox;

    @FXML
    private TextField InjuryDescriptionField;

    @FXML
    private ChoiceBox<Severity> SeverityBox;

    @FXML
    private DatePicker InjuryDatePicker;

    @FXML
    private Button addInjuryButton;

    @FXML
    public void initialize() {
        // Populate ChoiceBoxes with enum values
        SeverityBox.getItems().addAll(Severity.values());
        InjuryTypeBox.getItems().addAll(InjuryType.values());

        // Set default values (optional)
        SeverityBox.setValue(Severity.MILD);
        InjuryTypeBox.setValue(InjuryType.STRAIN);

        // Set button action
        addInjuryButton.setOnAction(event -> addInjury());

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

    private void addInjury() {
        InjuryServices injuryService = new InjuryServices();
        UserServices userService = new UserServices();

        String user_fname = AthleteNameField.getText();
        String user_lname = AthleteLastNameField.getText();
        String injury_description = InjuryDescriptionField.getText();
        Severity severity = SeverityBox.getValue();
        LocalDate injuryDate = InjuryDatePicker.getValue();
        InjuryType type = InjuryTypeBox.getValue();

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

        if (severity == null) {
            SeverityBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            SeverityBox.getStyleClass().remove("invalid-input");
        }

        if (injuryDate == null) {
            InjuryDatePicker.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            InjuryDatePicker.getStyleClass().remove("invalid-input");
        }

        if (type == null) {
            InjuryTypeBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            InjuryTypeBox.getStyleClass().remove("invalid-input");
        }

        if (!valid) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

        try (Connection con = MyDatabase.getInstance().getCon()) {
            // Get user_id based on the athlete's first and last name
            int user_id = userService.getUser_id(con, user_fname, user_lname);

            if (user_id == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Athlete not found.");
                return;
            }

            // Create an Injury object and populate it with the values from the form
            Injury injury = new Injury(user_id, type, injury_description, injuryDate, severity);
            injury.setUser_id(user_id);  // Set the user_id obtained from the database
            injury.setInjuryType(type);
            injury.setInjury_description(injury_description);
            injury.setInjuryDate(injuryDate);
            injury.setInjury_severity(severity);

            // Add injury to database
            injuryService.add(injury);  // Assuming `add` method in InjuryServices takes an Injury object as a parameter
            showAlert(Alert.AlertType.INFORMATION, "Success", "Injury added successfully!");

            // Clear fields after successful addition
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add injury.");
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
        InjuryDescriptionField.clear();
        InjuryDatePicker.setValue(null);
        SeverityBox.setValue(Severity.MODERATE);
        InjuryTypeBox.setValue(InjuryType.BRUISE);

        // Remove invalid-input class
        AthleteNameField.getStyleClass().remove("invalid-input");
        AthleteLastNameField.getStyleClass().remove("invalid-input");
        SeverityBox.getStyleClass().remove("invalid-input");
        InjuryDatePicker.getStyleClass().remove("invalid-input");
        InjuryTypeBox.getStyleClass().remove("invalid-input");
    }
}