package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.services.WeatherService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WeatherController implements Initializable {
    @FXML
    private TextField locationField;

    @FXML
    private Label weatherLabel;
    @FXML
    private ImageView getBack;

    private WeatherService weatherService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.weatherService = new WeatherService("");
        getBack.setOnMouseClicked(event -> switchBackToAddSession());

    }

    @FXML
    private void fetchWeather() {
        String location = locationField.getText().trim();
        if (!location.isEmpty()) {
            String weather = weatherService.getWeather(location);
            weatherLabel.setText(weather);
        } else {
            weatherLabel.setText("Please enter a location.");
        }
    }

    private void switchBackToAddSession() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addTrainingSession.fxml"));
            Parent root = loader.load();

            AddTrainingSession controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Welcome Coach");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) getBack.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open coach front screen: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
