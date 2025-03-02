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

        String verifyLoginQuery = "SELECT * FROM user WHERE user_email = ? AND user_pwd = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLoginQuery);
            preparedStatement.setString(1, username_id.getText());
            preparedStatement.setString(2, password_id.getText());

            ResultSet queryResult = preparedStatement.executeQuery();

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
            } else {
                error_id.setText("Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openUserInterface(String role) {
        try {
            Stage stage = (Stage) login_id.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Scene scene = null;

            switch (role.toLowerCase()) {
                case "coach":
                    loader.setLocation(getClass().getResource("/Coachfront.fxml"));
                    break;
                case "medical_staff":
                    loader.setLocation(getClass().getResource("/Medicalfront.fxml"));
                    break;
                case "athlete":
                    loader.setLocation(getClass().getResource("/Athletefront.fxml"));
                    break;
                default:
                    error_id.setText("Unknown role. Contact Admin.");
                    return;
            }
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
