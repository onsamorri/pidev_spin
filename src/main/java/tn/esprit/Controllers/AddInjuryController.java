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
        SeverityBox.getItems().addAll(Severity.values());
        InjuryTypeBox.getItems().addAll(InjuryType.values());
        SeverityBox.setValue(Severity.MILD);
        InjuryTypeBox.setValue(InjuryType.STRAIN);
        addInjuryButton.setOnAction(event -> addInjury());
        setDatePickerConstraints();
    }

    private void setDatePickerConstraints() {
        LocalDate now = LocalDate.now();
        LocalDate startOfAcademicYear = LocalDate.of(now.getYear(), 9, 1);
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

        if (user_fname.isEmpty() || user_lname.isEmpty() || severity == null || injuryDate == null || type == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

        try (Connection con = MyDatabase.getInstance().getCon()) {
            User user = userService.getUserByName(con, user_fname, user_lname);

            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Athlete not found.");
                return;
            }

            Injury injury = new Injury(user, type, injury_description, injuryDate, severity);
            injuryService.add(injury);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Injury added successfully!");
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
    }
}
