package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import tn.esprit.entities.Coach;
import tn.esprit.services.UserServices;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class addUser {

    @FXML
    private Button addUserBtn;

    @FXML
    private TextField email_id;

    @FXML
    private TextField fname_id;

    @FXML
    private TextField lname_id;

    @FXML
    private TextField password_id;

    @FXML
    private TextField phone_nb_id;

    private final UserServices userService = new UserServices(); // Initialize UserServices

    @FXML
    public void initialize() {
        // Attach event listener to the button
        addUserBtn.setOnAction(event -> addUserAction());
    }

    @FXML
    private void addUserAction() {
        if (!validateInputs()) {
            return;
        }
        Coach newCoach =null;

        String fname = fname_id.getText().trim();
        String lname = lname_id.getText().trim();
        String email = email_id.getText().trim();
        String password = password_id.getText().trim();
        String phoneNumber = phone_nb_id.getText().trim();
        String role = "coach";  // Role is always "coach"
        Integer nbTeams = 0;  // Default value



        newCoach = new Coach(fname, lname, email, password, phoneNumber, nbTeams);

        try {
            userService.add(newCoach);
            showAlert("Success", "Coach added successfully!");
            clearFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Error adding coach: " + e.getMessage());
        }
    }

    private void clearFields() {
        fname_id.clear();
        lname_id.clear();
        email_id.clear();
        password_id.clear();
        phone_nb_id.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean validateInputs() {
        String fname = fname_id.getText().trim();
        String lname = lname_id.getText().trim();
        String email = email_id.getText().trim();
        String password = password_id.getText().trim();
        String phoneNumber = phone_nb_id.getText().trim();

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

        return true;
    }




}
