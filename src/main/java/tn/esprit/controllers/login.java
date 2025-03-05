package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import tn.esprit.utils.MyDatabase;
import tn.esprit.entities.user;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.github.cage.Cage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class login {
    @FXML
    private Button cancel_id, login_id, refresh_id;
    @FXML
    private ImageView captcha;
    @FXML
    private TextField captcha_id, username_id;
    @FXML
    private PasswordField password_id;
    @FXML
    private Label error_id;

    private String captchaText;

    @FXML
    private void initialize() {
        updateCaptcha();
    }

    public void login_idOnAction(ActionEvent e) {
        String enteredCaptcha = captcha_id.getText();
        if (!validateCaptcha(enteredCaptcha)) {
            error_id.setText("Invalid CAPTCHA. Try again.");
            updateCaptcha(); // Refresh CAPTCHA on failure
            return;
        }
        if (!username_id.getText().isBlank() && !password_id.getText().isBlank()) {
=======
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import tn.esprit.utils.MyDatabase;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;


public class login {
    @FXML
    private Button cancel_id;

    @FXML
    private Button login_id;

    @FXML
    private PasswordField password_id;

    @FXML
    private TextField username_id;

    @FXML
    private Label error_id;

    public void login_idOnAction(ActionEvent e) {

        if (username_id.getText().isBlank() == false && password_id.getText().isBlank()==false) {
            //error_id.setText("Invalid username or password!");
>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
            validatelogin();
        } else {
            error_id.setText("Please enter Email and password");
        }
    }

    public void cancel_idOnAction(ActionEvent e) {
        Stage stage = (Stage) cancel_id.getScene().getWindow();
        stage.close();
    }

    public void validatelogin() {
        MyDatabase connectNow = new MyDatabase();
        Connection connectDB = connectNow.getCon();

<<<<<<< HEAD
        String verifyLoginQuery = "SELECT * FROM user WHERE user_email = ? AND user_pwd = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLoginQuery);
=======
        String verifyLoginQuery = "SELECT user_role FROM user WHERE user_email = ? AND user_pwd = ?";

        try {
            java.sql.PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLoginQuery);
>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
            preparedStatement.setString(1, username_id.getText());
            preparedStatement.setString(2, password_id.getText());

            ResultSet queryResult = preparedStatement.executeQuery();

<<<<<<< HEAD
            if (queryResult.next()) {
                user authenticatedUser = new user(
                        queryResult.getInt("user_id"),
                        queryResult.getString("user_fname"),
                        queryResult.getString("user_lname"),
                        queryResult.getString("user_email"),
                        queryResult.getString("user_pwd"),
                        queryResult.getString("user_nbr"),
                        user.user_role.valueOf(queryResult.getString("user_role").toUpperCase())
                );

                SessionManager.getInstance().setAuthenticatedUser(authenticatedUser);
                System.out.println("Authenticated user: " + authenticatedUser.getUser_fname() + " " + authenticatedUser.getUser_lname());
                error_id.setText("Login Successful");
                openUserInterface(authenticatedUser.getUser_role().toString());
=======
            if (queryResult.next()) { // If a record is found
                String userRole = queryResult.getString("user_role");
                error_id.setText("Login Successful");

                // Open the corresponding interface
                openUserInterface(userRole);

>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
            } else {
                error_id.setText("Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD

    private void openUserInterface(String role) {
        try {
            Stage stage = (Stage) login_id.getScene().getWindow();
=======
    private void openUserInterface(String role) {
        try {
            Stage stage = (Stage) login_id.getScene().getWindow(); // Get current stage
>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
            FXMLLoader loader = new FXMLLoader();
            Scene scene = null;

            switch (role.toLowerCase()) {
                case "coach":
                    loader.setLocation(getClass().getResource("/Coachfront.fxml"));
<<<<<<< HEAD
                    break;
                case "medical_staff":
                    loader.setLocation(getClass().getResource("/Medicalfront.fxml"));
                    break;
                case "athlete":
                    loader.setLocation(getClass().getResource("/Athletefront.fxml"));
=======
                    scene = new Scene(loader.load());
                    break;
                case "medical_staff":
                    loader.setLocation(getClass().getResource("/Medicalfront.fxml"));
                    scene = new Scene(loader.load());
                    break;
                case "athlete":
                    loader.setLocation(getClass().getResource("/Athletefront.fxml"));
                    scene = new Scene(loader.load());
>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
                    break;
                default:
                    error_id.setText("Unknown role. Contact Admin.");
                    return;
            }
<<<<<<< HEAD
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
=======

            stage.setScene(scene);
            stage.show();

>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
//captcha
    private void updateCaptcha() {
        Cage cage = new Cage();
        captchaText = cage.getTokenGenerator().next();
        byte[] imageData = cage.draw(captchaText);
        Image captchaImageSrc = new Image(new ByteArrayInputStream(imageData));
        captcha.setImage(captchaImageSrc);
    }

    @FXML
    private void refreshCaptcha(ActionEvent event) {
        updateCaptcha();
    }

    public boolean validateCaptcha(String userInput) {
        return userInput.equalsIgnoreCase(captchaText);
    }
=======


>>>>>>> 31bb8b24b66f6d59f3f1a023668c1ae5d3a3a67a
}
