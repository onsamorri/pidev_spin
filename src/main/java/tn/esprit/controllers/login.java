package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.json.JSONObject;
import tn.esprit.utils.MyDatabase;
import tn.esprit.entities.user;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class login {
    @FXML
    private Button cancel_id, login_id, refresh_id;
    @FXML
    private WebView recaptchaWebView;
    @FXML
    private TextField username_id;
    @FXML
    private PasswordField password_id;
    @FXML
    private Label error_id;



    @FXML
    public void initialize() {
        // Load the reCAPTCHA widget


    }

    public void login_idOnAction(ActionEvent e) {


        // Proceed with login logic
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

                String role = queryResult.getString("user_role").toLowerCase();
                System.out.println("Retrieved role from DB: " + role);

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

}

