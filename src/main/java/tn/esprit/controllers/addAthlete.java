package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Coach;
import tn.esprit.services.UserServices;
import tn.esprit.entities.Athlete;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;


public class addAthlete {

    @FXML
    private TextField Adress_id;

    @FXML
    private DatePicker DoB_id;

    @FXML
    private TextField Height_id;

    @FXML
    private TextField Weight_id;

    @FXML
    private Button addAthleteBtn;

    @FXML
    private TextField email_id;

    @FXML
    private TextField fname_id;

    @FXML
    private ComboBox<String> gender_id;

    @FXML
    private TextField lname_id;

    @FXML
    private TextField password_id;

    @FXML
    private TextField phone_nb_id;

    @FXML
    private TextField role_id1;
    private final UserServices userService = new UserServices(); // Initialize UserServices

    @FXML
    public void initialize() {
        // Attach event listener to the button
        addAthleteBtn.setOnAction(event -> addUserAction());
    }
    @FXML
    private void addUserAction() {
        String fname = fname_id.getText();
        String lname = lname_id.getText();
        String email = email_id.getText();
        String password = password_id.getText();
        String phoneNumber = phone_nb_id.getText();
        String role = "athlete";
        LocalDate athlete_DoB=DoB_id.getValue() ;
        String athlete_gender = gender_id.getValue();
        String athlete_address = Adress_id.getText();
        float athlete_height = Float.parseFloat(Height_id.getText());
        float athlete_weight = Float.parseFloat(Weight_id.getText());

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

        Athlete newAthlete = new Athlete(fname, lname, email, password, phoneNumber, Date.valueOf(athlete_DoB), athlete_gender, athlete_address, athlete_height, athlete_weight, 0);

        try {
            userService.add(newAthlete);
            showAlert("Success", "Athlete added successfully!");
            clearFields();
        } catch (SQLException e) {
            showAlert("Database Error", "Error adding Athlete: " + e.getMessage());
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z]+$");
    }

    private boolean isValidPassword(String password) {
        // At least one letter, one number, one special character, and at least 6 characters long
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
        return Pattern.matches(passwordRegex, password);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+\\d+$");
    }

    private void clearFields() {
        fname_id.clear();
        lname_id.clear();
        email_id.clear();
        password_id.clear();
        phone_nb_id.clear();
        Adress_id.clear();
        Height_id.clear();
        Weight_id.clear();
        DoB_id.getEditor().clear();
        gender_id.getSelectionModel().clearSelection();

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
