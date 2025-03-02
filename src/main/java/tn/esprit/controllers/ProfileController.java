package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.entities.user;

public class ProfileController {

    @FXML
    private Button logout_id;

    @FXML
    private void initialize() {
        user currentUser = SessionManager.getInstance().getAuthenticatedUser();
        if (currentUser == null) {
            System.out.println("No active session. Redirecting to login...");
            redirectToLoginScreen();  // Redirect to login if no active session
        } else {
            System.out.println("Active session");
        }
    }

    @FXML
    private void handleProfileClick() {
        System.out.println("Profile Image Clicked");
        user currentUser = SessionManager.getInstance().getAuthenticatedUser();

        if (currentUser == null) {
            System.out.println("No active session. Redirecting to login...");
            redirectToLoginScreen();  // Redirect to login if no active session
            return;
        }

        System.out.println("User role: " + currentUser.getUser_role().toString());
        openProfileScreen(currentUser);
    }

    private void openProfileScreen(user authenticatedUser) {
        try {
            Stage stage = (Stage) logout_id.getScene().getWindow();  // Get the current stage

            FXMLLoader loader = new FXMLLoader();
            Scene scene = null;

            switch (authenticatedUser.getUser_role().toString().toLowerCase()) {
                case "coach":
                    loader.setLocation(getClass().getResource("/profileCoach.fxml"));
                    break;
                case "medical_staff":
                    loader.setLocation(getClass().getResource("/profileMedical.fxml"));
                    break;
                case "athlete":
                    loader.setLocation(getClass().getResource("/profileAthlete.fxml"));
                    break;
                default:
                    System.out.println("Unknown role. Contact Admin.");
                    return;
            }

            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout_idOnAction(ActionEvent event) {
        // Call the SessionManager to clear the session
        SessionManager.getInstance().logout();
        // Test if session is cleared
        user currentUserAfterLogout = SessionManager.getInstance().getAuthenticatedUser();
        if (currentUserAfterLogout == null) {
            System.out.println("Logout successful. Session is cleared.");
        } else {
            System.out.println("Logout failed. Session still active.");
        }
        redirectToLoginScreen();
    }

    private void redirectToLoginScreen() {
        try {
            Stage stage = (Stage) logout_id.getScene().getWindow();  // Get current stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
