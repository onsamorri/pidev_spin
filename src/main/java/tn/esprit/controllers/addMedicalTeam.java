package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import tn.esprit.entities.Medical_staff;
import tn.esprit.services.UserServices;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class addMedicalTeam {

    @FXML
    private Button addStaffBtn;

    @FXML
    private TextField email_id1;

    @FXML
    private TextField fname_id1;

    @FXML
    private TextField lname_id1;

    @FXML
    private TextField password_id1;

    @FXML
    private TextField phone_nb_id1;

    @FXML
    private ComboBox<String> specialty_id;

    private final UserServices userService = new UserServices(); // Initialize UserServices

    @FXML
    private void initialize() {
        // Attach event listener to the button
        addStaffBtn.setOnAction(event -> addStaffAction());
    }

    @FXML
    private void addStaffAction() {
        if (!validateInputs()) {
            return;
        }

        try {
            Medical_staff newStaff = null;
            String fname = fname_id1.getText().trim();
            String lname = lname_id1.getText().trim();
            String email = email_id1.getText().trim();
            String password = password_id1.getText().trim();
            String phoneNumber = phone_nb_id1.getText().trim();
            String specialty = specialty_id.getValue();
            String role = "medical_staff"; // Always "medical_staff"



            // Create a MedicalStaff object
            newStaff = new Medical_staff(fname, lname, email, password, phoneNumber, specialty);

            userService.add(newStaff);
            showAlert("Success", "Medical staff added successfully!");
            clearFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Error adding medical staff: " + e.getMessage());
        }
    }
    private boolean validateInputs() {
        String fname = fname_id1.getText().trim();
        String lname = lname_id1.getText().trim();
        String email = email_id1.getText().trim();
        String password = password_id1.getText().trim();
        String phoneNumber = phone_nb_id1.getText().trim();
        String specialty = specialty_id.getValue();

        // Validate first name
        if (fname.isEmpty() || !fname.matches("[a-zA-Z]+")) {
            showAlert("Validation Error", "First name should only contain letters (no numbers or special characters).");
            return false;
        }

        // Validate last name
        if (lname.isEmpty() || !lname.matches("[a-zA-Z]+")) {
            showAlert("Validation Error", "Last name should only contain letters (no numbers or special characters).");
            return false;
        }

        // Validate email
        if (!email.contains("@") || !email.contains(".") || email.startsWith("@") || email.endsWith(".")) {
            showAlert("Validation Error", "Invalid email format. Example: example@domain.com");
            return false;
        }

        // Validate password (at least 8 characters, one uppercase, one number, one special character)
        if (password.length() < 8) {
            showAlert("Validation Error", "Password must be at least 8 characters long.");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            showAlert("Validation Error", "Password must contain at least one uppercase letter.");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showAlert("Validation Error", "Password must contain at least one number.");
            return false;
        }
        if (!password.matches(".*[@$!%*?&].*")) {
            showAlert("Validation Error", "Password must contain at least one special character (@, $, !, %, *, ?, &).");
            return false;
        }

        // Validate phone number (must start with + and have at least 8 digits)
        if (!phoneNumber.startsWith("+") || phoneNumber.length() < 9 || !phoneNumber.substring(1).matches("\\d+")) {
            showAlert("Validation Error", "Phone number must start with '+' and contain at least 8 digits.");
            return false;
        }
        if(specialty_id.getValue() == null) {
            showAlert("Validation Error", "Please select a specialty.");
            return false;
        }

        return true;
    }

    private void clearFields() {
        fname_id1.clear();
        lname_id1.clear();
        email_id1.clear();
        password_id1.clear();
        phone_nb_id1.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
