package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyInjuriesController {

    @FXML
    private Label injuryTypeLabel;
    @FXML
    private Label injuryDateLabel;
    @FXML
    private Label injurySeverityLabel;
    @FXML
    private Label recoveryPlanLabel;
    @FXML
    private Label recoveryStatusLabel;
    @FXML
    private Label BackButton;

    private static final int USER_ID = 38;

    public void initialize() {
        loadInjuryData();
        loadRecoveryPlanData();

        BackButton.setOnMouseClicked(event -> switchScreenToBack());

    }

    private void switchScreenToBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Athletefront.fxml"));
            Parent root = loader.load();

            Athletefront controller = loader.getController();
            controller.initialize();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Back");
            stage.setUserData(this);
            stage.show();

            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Athletefront screen: " + e.getMessage());
        }
    }


    private void loadInjuryData() {
        String query = "SELECT injuryType, injuryDate, injury_severity FROM injury WHERE user_id = ?";
        try (Connection con = MyDatabase.getInstance().getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, USER_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                injuryTypeLabel.setText(rs.getString("injuryType"));
                injuryDateLabel.setText(rs.getString("injuryDate"));
                injurySeverityLabel.setText(rs.getString("injury_severity"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading injury data: " + e.getMessage());
        }
    }

    private void loadRecoveryPlanData() {
        String query = "SELECT recovery_Goal, recovery_Status FROM recoveryplan WHERE user_id = ?";
        try (Connection con = MyDatabase.getInstance().getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, USER_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                recoveryPlanLabel.setText(rs.getString("recovery_Goal"));
                recoveryStatusLabel.setText(rs.getString("recovery_Status"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading recovery plan data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
