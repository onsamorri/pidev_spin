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
        String fname = fname_id1.getText().trim();
        String lname = lname_id1.getText().trim();
        String email = email_id1.getText().trim();
        String password = password_id1.getText().trim();
        String phoneNumber = phone_nb_id1.getText().trim();
        String specialty = specialty_id.getValue();
        String role = "medical_staff"; // Always "medical_staff"

        // Validate input
        if (!isValidName(fname)) {
            showAlert("Validation Error", "First name should only contain letters.");
            return;
        }

        if (!isValidName(lname)) {
            showAlert("Validation Error", "Last name should only contain letters.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Validation Error", "Invalid email format. Example: example@domain.com");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert("Validation Error", "Password must contain letters, numbers, and special characters.");
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            showAlert("Validation Error", "Phone number must start with '+' followed by digits.");
            return;
        }


        // Create a MedicalStaff object
        Medical_staff newStaff = new Medical_staff(fname, lname, email, password, phoneNumber, specialty);

        try {
            userService.add(newStaff);
            showAlert("Success", "Medical staff added successfully!");
            clearFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Error adding medical staff: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9])[A-Za-z\\d@#$%^&+=*!]{6,}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+\\d+$");
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
