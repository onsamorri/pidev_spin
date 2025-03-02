package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.concurrent.Task;

public class RecoveryChatBotController {

    @FXML
    private VBox messageContainer;  // VBox to hold all messages
    @FXML
    private TextField userInputField;  // TextField where the user types their message
    @FXML
    private Button sendButton;  // Button to send the message
    @FXML
    private ScrollPane chatScrollPane;  // Scrollable area for messages

    // Method to handle the send button click
    @FXML
    public void sendMessage() {
        // Get the user's input
        String userMessage = userInputField.getText().trim();  // Trim to remove leading/trailing spaces

        // Edge case: If user message is empty, don't proceed
        if (userMessage.isEmpty()) {
            addMessageBubble("Please type something to continue...", false);
            return;
        }

        userInputField.clear();  // Clear the input field after sending

        // Add user message to the container as a "bubble"
        addMessageBubble(userMessage, true);

        // Start async task to simulate the assistant's response
        Task<String> assistantTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                // Simulate processing and returning a response after a delay
                Thread.sleep(1000);  // Simulate response delay
                return getAssistantResponse(userMessage);
            }
        };

        // Handle the response on the JavaFX Application Thread
        assistantTask.setOnSucceeded(event -> {
            String assistantResponse = assistantTask.getValue();
            addMessageBubble(assistantResponse, false);
        });

        // Run the task in the background to avoid blocking the UI
        new Thread(assistantTask).start();
    }

    // Helper method to add a message bubble to the chat
    private void addMessageBubble(String message, boolean isUser) {
        // Create a label for the message
        Label messageLabel = new Label(message);

        // Style the message bubble (based on whether it's the user or assistant)
        messageLabel.setStyle("-fx-background-color: " +
                (isUser ? "#a5d6a7" : "#e3f2fd") + "; " +  // Different colors for user/assistant
                "-fx-padding: 10px; -fx-background-radius: 15px; -fx-text-fill: #000; -fx-font-size: 14px;");

        // Create an HBox to hold the message label
        HBox messageHBox = new HBox();
        if (isUser) {
            messageHBox.setStyle("-fx-alignment: CENTER-RIGHT;");
        } else {
            messageHBox.setStyle("-fx-alignment: CENTER-LEFT;");
        }
        messageHBox.getChildren().add(messageLabel);

        // Add the message box to the VBox (chat container)
        messageContainer.getChildren().add(messageHBox);

        // Scroll to the latest message
        chatScrollPane.setVvalue(1.0);
    }

    // Expanded method to return a more varied assistant response based on user input
    private String getAssistantResponse(String userMessage) {
        if (userMessage.toLowerCase().contains("mid phase")) {
            return "You're in the Mid phase! Here's your diet plan for the day: High Protein. Keep up the good work! 👍";
        } else if (userMessage.toLowerCase().contains("early phase")) {
            return "You're in the Early phase! Focus on rest and light activity. Stay positive! 💪";
        } else if (userMessage.toLowerCase().contains("late phase")) {
            return "You're in the Late phase! Keep up with your exercises and nutrition. You're almost there! 🎯";
        } else if (userMessage.toLowerCase().contains("help")) {
            return "Sure! How can I assist you with your recovery today? 😊";
        } else if (userMessage.toLowerCase().contains("diet")) {
            return "I recommend a balanced diet with lean proteins and lots of veggies. Drink plenty of water! 💧";
        } else {
            return "I didn’t quite understand that. Could you rephrase? 🤔";
        }
    }
}
