package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.services.DeepSeekServices;

public class DeepSeekController {

    @FXML
    private TextField userMessage;

    @FXML
    private TextArea deepSeekResponse;

    @FXML
    private ImageView backBtn;

    private DeepSeekServices deepSeekService = new DeepSeekServices();

    @FXML
    private void initialize() {
        // Set the event handler for backBtn
        backBtn.setOnMouseClicked(event -> goBack());
    }

    @FXML
    private void sendMessage() {
        String model = "deepseek-r1:1.5b";
        String prompt = userMessage.getText();

        // Get response from DeepSeekService
        String fullResponse = deepSeekService.getResponseFromAPI(model, prompt);

        // Display the response in the deepSeekResponse TextArea
        deepSeekResponse.setText(fullResponse);

        // Clear the userMessage TextField
        userMessage.clear();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Coachfront.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Welcome Coach");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}