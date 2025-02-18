package tn.esprit.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tn.esprit.entities.Injury;
import tn.esprit.entities.InjuryType;
import tn.esprit.entities.Severity;
import tn.esprit.services.InjuryServices;
import tn.esprit.services.AthleteServices;
import tn.esprit.services.MedicalStaffServices;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddInjuryController implements Initializable {

    @FXML
    private ComboBox<InjuryType> injuryTypeCombo;

    @FXML
    private ComboBox<Severity> injurySeverityCombo;

    @FXML
    private TextArea injuryDescField;

    @FXML
    private DatePicker injuryDatePicker;

    @FXML
    private TextField athleteNameField;

    @FXML
    private TextField medicalStaffNameField;

    @FXML
    private Button addInjuryButton;

    private final InjuryServices injuryService = new InjuryServices();
    private final AthleteService athleteService = new AthleteService(); // Service to fetch athlete data
    private final MedicalStaffService medicalStaffService = new MedicalStaffService(); // Service to fetch medical staff data

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate InjuryType ComboBox
        ObservableList<InjuryType> injuryTypes = FXCollections.observableArrayList(InjuryType.values());
        injuryTypeCombo.setItems(injuryTypes);

        // Populate Severity ComboBox
        ObservableList<Severity> severities = FXCollections.observableArrayList(Severity.values());
        injurySeverityCombo.setItems(severities);
    }

    @FXML
    private void handleAddInjury(ActionEvent event) {
        try {
            String athleteName = athleteNameField.getText();
            String medicalStaffName = medicalStaffNameField.getText();

            // Fetch athlete and medical staff IDs from their names
            int athleteId = athleteService.getAthleteIdByName(athleteName);
            int medicalStaffId = medicalStaffService.getMedicalStaffIdByName(medicalStaffName);

            if (athleteId == -1 || medicalStaffId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid athlete or medical staff name.");
                return;
            }

            InjuryType selectedType = injuryTypeCombo.getValue();
            Severity selectedSeverity = injurySeverityCombo.getValue();
            String description = injuryDescField.getText();
            java.sql.Date date = java.sql.Date.valueOf(injuryDatePicker.getValue());

            if (selectedType == null || selectedSeverity == null || description.isEmpty() || date == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields correctly.");
                return;
            }

            Injury newInjury = new Injury(selectedType, selectedSeverity, description, date, athleteId, medicalStaffId);
            injuryService.addInjury(newInjury);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Injury added successfully!");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching data: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    private void clearFields() {
        injuryTypeCombo.getSelectionModel().clearSelection();
        injurySeverityCombo.getSelectionModel().clearSelection();
        injuryDescField.clear();
        injuryDatePicker.setValue(null);
        athleteNameField.clear();
        medicalStaffNameField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
