package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.entities.Medical_staff;
import tn.esprit.entities.user;
import tn.esprit.services.UserServices;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class updateMedicalteam {

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
    private TextField role_id1;

    @FXML
    private ComboBox<String> specialty_id;

    @FXML
    private Button updateStaffBtn;

    private Medical_staff selectedStaff;
    private final UserServices userServices = new UserServices();

    public void initData(Medical_staff m) {
        updateStaffBtn.setOnAction(event -> updateStaffAction());
        this.selectedStaff = m;
        fname_id1.setText(m.getUser_fname());
        lname_id1.setText(m.getUser_lname());
        email_id1.setText(m.getUser_email());
        password_id1.setText(m.getUser_pwd());
        phone_nb_id1.setText(m.getUser_nbr());
        specialty_id.setValue(m.getSpeciality());

    }
    @FXML
    private void updateStaffAction() {
        /*if (!validateInputs()) {
            return;
        }*/
        selectedStaff.setUser_fname(fname_id1.getText());
        selectedStaff.setUser_lname(lname_id1.getText());
        selectedStaff.setUser_email(email_id1.getText());
        selectedStaff.setUser_pwd(password_id1.getText());
        selectedStaff.setUser_nbr(phone_nb_id1.getText());
        selectedStaff.setSpeciality(specialty_id.getValue());
        selectedStaff.setUser_role(user.user_role.MEDICAL_STAFF);

        userServices.update(selectedStaff.getUser_id(), selectedStaff);
        System.out.println("Medical staff updated successfully!");

        // Close the update window
        updateStaffBtn.getScene().getWindow().hide();

    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*private boolean validateInputs() {
        boolean valid = true;
        String namePattern = "^[a-zA-Z]+$";

        if (teamNameU.getText().isEmpty() || !Pattern.matches(namePattern, teamNameU.getText())) {
            showAlert("Validation Error", "Team name must contain only letters and cannot be empty.");
            valid = false;
        } else if (teamTOSU.getValue() == null) {
            showAlert("Validation Error", "Please select a sport.");
            valid = false;
        }
        try {
            Integer.parseInt(teamLossesU.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Losses must be a valid integer.");
            valid = false;
        }

        try {
            Integer.parseInt(teamWinsU.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Wins must be a valid integer.");
            valid = false;
        }


        return valid;
}*/
}