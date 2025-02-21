package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.entities.Coach;
import tn.esprit.entities.user;
import tn.esprit.services.UserServices;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class updateCoach {

    @FXML
    private Button addUserBtn;

    @FXML
    private TextField email_id;

    @FXML
    private TextField fname_id;

    @FXML
    private TextField lname_id;

    @FXML
    private TextField nbteams_id;

    @FXML
    private TextField password_id;

    @FXML
    private TextField phone_nb_id;

    @FXML
    private TextField role_id1;

    private Coach selectedCoach;
    private final UserServices userServices = new UserServices();

    public void initData(Coach c) {
        addUserBtn.setOnAction(event -> updateCoachAction());
        this.selectedCoach = c;
        fname_id.setText(c.getUser_fname());
        lname_id.setText(c.getUser_lname());
        email_id.setText(c.getUser_email());
        password_id.setText(c.getUser_pwd());
        phone_nb_id.setText(c.getUser_nbr());
        nbteams_id.setText(String.valueOf(c.getNb_teams()));

    }
    @FXML
    private void updateCoachAction() {
        /*if (!validateInputs()) {
            return;
        }*/
        selectedCoach.setUser_fname(fname_id.getText());
        selectedCoach.setUser_lname(lname_id.getText());
        selectedCoach.setUser_email(email_id.getText());
        selectedCoach.setUser_pwd(password_id.getText());
        selectedCoach.setUser_nbr(phone_nb_id.getText());
        selectedCoach.setNb_teams(Integer.parseInt(nbteams_id.getText()));
        selectedCoach.setUser_role(user.user_role.COACH);

        userServices.update(selectedCoach.getUser_id(), selectedCoach);
        System.out.println("Coach updated successfully!");

        // Close the update window
        addUserBtn.getScene().getWindow().hide();

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