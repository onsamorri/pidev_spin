package tn.esprit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Claim;
import tn.esprit.entities.ClaimCategory;
import tn.esprit.entities.ClaimStatus;
import tn.esprit.services.ClaimServices;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddClaimController {

    @FXML
    private TextField claimDescriptionField;

    @FXML
<<<<<<< HEAD
=======
    private ChoiceBox<ClaimStatus> claimStatusBox;

    @FXML
>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4
    private DatePicker claimDatePicker;

    @FXML
    private ChoiceBox<ClaimCategory> claimCategoryBox;

    @FXML
    private Button addClaimButton;

    @FXML
    public void initialize() {
<<<<<<< HEAD
        // Populate ChoiceBox with enum values
        claimCategoryBox.getItems().addAll(ClaimCategory.values());

        // Set default values (optional)
=======
        // Populate ChoiceBoxes with enum values
        claimStatusBox.getItems().addAll(ClaimStatus.values());
        claimCategoryBox.getItems().addAll(ClaimCategory.values());

        // Set default values (optional)
        claimStatusBox.setValue(ClaimStatus.IN_REVIEW);
>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4
        claimCategoryBox.setValue(ClaimCategory.MISCONDUCT);

        // Set button action
        addClaimButton.setOnAction(event -> addClaim());

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
        claimDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(finalStartOfAcademicYear));
            }
        });
    }

    private void addClaim() {
        ClaimServices claimService = new ClaimServices();

        String description = claimDescriptionField.getText();
<<<<<<< HEAD
=======
        ClaimStatus status = claimStatusBox.getValue();
>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4
        LocalDate claimDate = claimDatePicker.getValue();
        ClaimCategory category = claimCategoryBox.getValue();

        // Validate inputs
        boolean valid = true;

        if (description.isEmpty()) {
            claimDescriptionField.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimDescriptionField.getStyleClass().remove("invalid-input");
        }

<<<<<<< HEAD
=======
        if (status == null) {
            claimStatusBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimStatusBox.getStyleClass().remove("invalid-input");
        }

>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4
        if (claimDate == null) {
            claimDatePicker.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimDatePicker.getStyleClass().remove("invalid-input");
        }

        if (category == null) {
            claimCategoryBox.getStyleClass().add("invalid-input");
            valid = false;
        } else {
            claimCategoryBox.getStyleClass().remove("invalid-input");
        }

        if (!valid) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields correctly.");
            return;
        }

<<<<<<< HEAD
        // Create Claim object with status IN_REVIEW
        Claim claim = new Claim(description, ClaimStatus.IN_REVIEW, claimDate, category);
=======
        // Create Claim object
        Claim claim = new Claim(description, status, claimDate, category);
>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4

        try {
            // Add claim to database
            claimService.add(claim);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Claim added successfully!");

            // Clear fields after successful addition
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add claim.");
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
        claimDescriptionField.clear();
        claimDatePicker.setValue(null);
<<<<<<< HEAD
=======
        claimStatusBox.setValue(ClaimStatus.IN_REVIEW);
>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4
        claimCategoryBox.setValue(ClaimCategory.MISCONDUCT);

        // Remove invalid-input class
        claimDescriptionField.getStyleClass().remove("invalid-input");
<<<<<<< HEAD
=======
        claimStatusBox.getStyleClass().remove("invalid-input");
>>>>>>> 25b00d9d2a12642a04c47dfc83611927232072c4
        claimDatePicker.getStyleClass().remove("invalid-input");
        claimCategoryBox.getStyleClass().remove("invalid-input");
    }
}