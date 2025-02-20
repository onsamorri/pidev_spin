package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Claim;
import tn.esprit.entities.ClaimAction;
import tn.esprit.entities.ClaimActionType;
import tn.esprit.services.ClaimActionServices;
import tn.esprit.services.ClaimServices;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddClaimActionController {

    @FXML
    private TextField claimIdField;

    @FXML
    private ChoiceBox<ClaimActionType> claimActionTypeBox;

    @FXML
    private DatePicker claimActionStartDatePicker;

    @FXML
    private DatePicker claimActionEndDatePicker;

    @FXML
    private TextField claimActionNotesField;

    @FXML
    private Button addClaimActionButton;

    @FXML
    public void initialize() {
        // Populate ChoiceBox with enum values
        claimActionTypeBox.getItems().addAll(ClaimActionType.values());

        // Set default values (optional)
        claimActionTypeBox.setValue(ClaimActionType.NO_ACTION_TAKEN);

        // Set button action
        addClaimActionButton.setOnAction(event -> addClaimAction());

        // Set the date picker constraints
        setDatePickerConstraints();

        // Add listeners to date pickers for dynamic validation
        addDateValidationListeners();
    }

    public void setClaimData(Claim claim) {
        claimIdField.setText(String.valueOf(claim.getClaimId()));
        claimIdField.setDisable(true); // Disable editing of claim ID
    }

    private void setDatePickerConstraints() {
        LocalDate today = LocalDate.now();

        // Set the claimActionStartDatePicker to not allow dates before today
        claimActionStartDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(today));
            }
        });

        // Set the claimActionEndDatePicker to not allow dates before the selected start date
        claimActionStartDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            claimActionEndDatePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    setDisable(empty || item.isBefore(newValue));
                }
            });
        });
    }

    private void addDateValidationListeners() {
        claimActionStartDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && claimActionEndDatePicker.getValue() != null && newValue.isAfter(claimActionEndDatePicker.getValue())) {
                showAlert(Alert.AlertType.ERROR, "Date Error", "Start date cannot be after end date.");
                claimActionStartDatePicker.setValue(oldValue); // Revert to the old value
            }
        });

        claimActionEndDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && claimActionStartDatePicker.getValue() != null && newValue.isBefore(claimActionStartDatePicker.getValue())) {
                showAlert(Alert.AlertType.ERROR, "Date Error", "End date cannot be before start date.");
                claimActionEndDatePicker.setValue(oldValue); // Revert to the old value
            }
        });
    }

    private void addClaimAction() {
        ClaimActionServices claimActionService = new ClaimActionServices();
        ClaimServices claimService = new ClaimServices();

        int claimId;
        try {
            claimId = Integer.parseInt(claimIdField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please enter a valid claim ID.");
            return;
        }

        ClaimActionType actionType = claimActionTypeBox.getValue();
        LocalDate startDate = claimActionStartDatePicker.getValue();
        LocalDate endDate = claimActionEndDatePicker.getValue();
        String notes = claimActionNotesField.getText();

        // Validate inputs
        boolean valid = true;

        if (claimIdField.getText().isEmpty()) {
            claimIdField.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimIdField.getStyleClass().remove("invalid-input");
        }

        if (actionType == null) {
            claimActionTypeBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimActionTypeBox.getStyleClass().remove("invalid-input");
        }

        if (startDate == null) {
            claimActionStartDatePicker.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimActionStartDatePicker.getStyleClass().remove("invalid-input");
        }

        if (endDate == null) {
            claimActionEndDatePicker.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimActionEndDatePicker.getStyleClass().remove("invalid-input");
        }

        if (notes.isEmpty()) {
            claimActionNotesField.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimActionNotesField.getStyleClass().remove("invalid-input");
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.ERROR, "Date Error", "Start date cannot be after end date.");
            return; // Exit the method to prevent the second message from appearing
        }

        if (!valid) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

        // Retrieve the claim object
        Claim claim;
        try {
            claim = claimService.findById(claimId);
            if (claim == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "No claim found with the given ID.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve claim.");
            return;
        }

        // Create ClaimAction object
        ClaimAction claimAction = new ClaimAction(claim, actionType, startDate, endDate, notes);

        try {
            // Add claim action to database
            claimActionService.add(claimAction);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Claim action added successfully!");

            // Clear fields after successful addition
            clearFields();

            // Close the window after successful addition
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add claim action.");
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
        claimIdField.clear();
        claimActionTypeBox.setValue(ClaimActionType.NO_ACTION_TAKEN);
        claimActionStartDatePicker.setValue(null);
        claimActionEndDatePicker.setValue(null);
        claimActionNotesField.clear();

        // Remove invalid-input class
        claimIdField.getStyleClass().remove("invalid-input");
        claimActionTypeBox.getStyleClass().remove("invalid-input");
        claimActionStartDatePicker.getStyleClass().remove("invalid-input");
        claimActionEndDatePicker.getStyleClass().remove("invalid-input");
        claimActionNotesField.getStyleClass().remove("invalid-input");
    }

    private void closeWindow() {
        Stage stage = (Stage) addClaimActionButton.getScene().getWindow();
        stage.close();
    }
}