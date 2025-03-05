package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.concurrent.Task;
import tn.esprit.entities.*;
import tn.esprit.services.RecoveryRulesChatBotServices;
import tn.esprit.controllers.SessionManager;

import java.util.List;

public class RecoveryChatBotController {
    @FXML private VBox messageContainer;
    @FXML private TextField userInputField;
    @FXML private Button sendButton;
    @FXML private ScrollPane chatScrollPane;

    private RecoveryRulesChatBotServices chatbotService = new RecoveryRulesChatBotServices();
    private user currentUser = SessionManager.getInstance().getAuthenticatedUser();

    @FXML
    public void sendMessage() {
        String userMessage = userInputField.getText().trim();
        if (userMessage.isEmpty()) return;

        userInputField.clear();
        addMessageBubble(userMessage, true);

        Task<String> assistantTask = new Task<>() {
            @Override protected String call() {
                return chatbotService.getChatBotResponse(userMessage, currentUser);
            }
        };

        assistantTask.setOnSucceeded(event -> addMessageBubble(assistantTask.getValue(), false));
        new Thread(assistantTask).start();
    }

    private void addMessageBubble(String message, boolean isUser) {
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-background-color: " + (isUser ? "#a5d6a7" : "#e3f2fd") +
                "; -fx-padding: 10px; -fx-background-radius: 15px; -fx-text-fill: #000;");
        HBox messageHBox = new HBox(messageLabel);
        messageHBox.setStyle(isUser ? "-fx-alignment: CENTER-RIGHT;" : "-fx-alignment: CENTER-LEFT;");
        Platform.runLater(() -> {
            messageContainer.getChildren().add(messageHBox);
            chatScrollPane.setVvalue(1.0);
        });
    }
}

