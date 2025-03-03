package tn.esprit.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.entities.user;

public class SessionManager {
    private static SessionManager instance;
    private user authenticatedUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setAuthenticatedUser(user user) {
        this.authenticatedUser = user;
    }

    public user getAuthenticatedUser() {
        return this.authenticatedUser;
    }

    // Method to clear the session (Logout)
    public void logout() {
        this.authenticatedUser = null;  // Clear the session
    }

    // Helper method to open the profile based on the user's role
    public static void openProfileBasedOnRole() {
        user currentUser = SessionManager.getInstance().getAuthenticatedUser();
        if (currentUser == null) {
            System.out.println("No active session. Redirecting to login...");
            return;
        }

        String fxmlFile = "";
        String title = "";

        switch (currentUser.getUser_role()) {
            case COACH:
                fxmlFile = "/profileCoach.fxml";
                title = "Coach Profile";
                break;
            case ATHLETE:
                fxmlFile = "/profileAthlete.fxml";
                title = "Athlete Profile";
                break;
            case MEDICAL_STAFF:
                fxmlFile = "/profileMedical.fxml";
                title = "Medical Staff Profile";
                break;
            default:
                System.out.println("Unknown role. Cannot open profile.");
                return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(SessionManager.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}