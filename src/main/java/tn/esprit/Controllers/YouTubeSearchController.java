package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class YouTubeSearchController {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button BackButton;
    @FXML
    private WebView webView;
    @FXML
    private Button refreshButton;

    private WebEngine webEngine;
    private String currentQuery = ""; // Store the current search query

    public void initialize() {
        webEngine = webView.getEngine();

        BackButton.setOnMouseClicked(event -> switchScreenToBack());
        searchButton.setOnAction(event -> searchYouTube());
        refreshButton.setOnAction(event -> refreshYouTubeSearch()); // Set action for refreshButton
    }

    private void searchYouTube() {
        String query = searchField.getText();
        if (query != null && !query.trim().isEmpty()) {
            currentQuery = query; // Save the search query
            String searchUrl = "https://www.youtube.com/results?search_query=" + query.replace(" ", "+");
            webEngine.load(searchUrl);
        }
    }

    private void refreshYouTubeSearch() {
        if (!currentQuery.isEmpty()) {
            String searchUrl = "https://www.youtube.com/results?search_query=" + currentQuery.replace(" ", "+");
            webEngine.load(searchUrl); // Reload the current search
        } else {
            showAlert("Error", "No search query to refresh.");
        }
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
            stage.show();

            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open Athletefront screen: " + e.getMessage());
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
