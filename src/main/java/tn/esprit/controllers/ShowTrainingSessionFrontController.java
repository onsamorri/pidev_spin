package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.services.QRCodeService;
import tn.esprit.services.TrainingSessionServices;
import tn.esprit.entities.TrainingSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class ShowTrainingSessionFrontController {

    @FXML
    private VBox vbox;
    @FXML
    private ImageView getBack;

    private final TrainingSessionServices trainingSessionService = new TrainingSessionServices();
    private final QRCodeService qrCodeService = new QRCodeService(); // QR Code Generator
    private final String[] imagePaths = {
            "/trainingSessionImages/01.jpg",
            "/trainingSessionImages/02.jpg",
            "/trainingSessionImages/03.jpg",
            "/trainingSessionImages/04.jpg",
            "/trainingSessionImages/05.jpg",
            "/trainingSessionImages/06.jpg",
            "/trainingSessionImages/07.jpg"
    };
    private final Random random = new Random();

    @FXML
    public void initialize() throws SQLException {
        loadTrainingSessions();
        getBack.setOnMouseClicked(event -> switchBackToAthletefront());

    }

    private void loadTrainingSessions() throws SQLException {
        List<TrainingSession> sessions = trainingSessionService.returnList(); // Fetch from DB
        vbox.getChildren().clear(); // Clear previous content to avoid duplicates

        if (sessions.isEmpty()) {
            Label noDataLabel = new Label("No training sessions available.");
            noDataLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: gray;");
            vbox.getChildren().add(noDataLabel);
            return;
        }

        for (TrainingSession session : sessions) {
            VBox sessionBox = new VBox();
            sessionBox.setSpacing(10);
            sessionBox.setPadding(new Insets(10));
            sessionBox.setAlignment(javafx.geometry.Pos.CENTER);
            sessionBox.setPrefWidth(550);
            sessionBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: lightgray; -fx-border-radius: 15; -fx-padding: 10;");

            ImageView imageView = new ImageView();
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-background-radius: 10;");

            // Pick a random image from the predefined set
            String randomImagePath = imagePaths[random.nextInt(imagePaths.length)];
            imageView.setImage(new Image(getClass().getResource(randomImagePath).toString()));

            Label sessionLocation = new Label(session.getLocation()); // Display session location
            sessionLocation.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label getMoreInfo = new Label("Get more info+");
            getMoreInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: blue; -fx-underline: true;");
            getMoreInfo.setOnMouseClicked(event -> showQRCodePopup(session));

            sessionBox.getChildren().addAll(imageView, sessionLocation, getMoreInfo);
            vbox.getChildren().add(sessionBox); // Add each session inside the VBox (white box)
        }
    }

    /**
     * Displays a pop-up with a QR code for the selected training session.
     */
    private void showQRCodePopup(TrainingSession session) {
        Stage qrStage = new Stage();
        qrStage.initModality(Modality.APPLICATION_MODAL);
        qrStage.setTitle("Training Session QR Code");

        VBox popupVBox = new VBox(10);
        popupVBox.setPadding(new Insets(15));
        popupVBox.setAlignment(javafx.geometry.Pos.CENTER);
        popupVBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 10;");

        Label titleLabel = new Label("Scan this QR Code for session details:");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        String qrCodeUrl = qrCodeService.generateQRCode(session);
        ImageView qrImageView = new ImageView(new Image(qrCodeUrl));
        qrImageView.setFitHeight(200);
        qrImageView.setFitWidth(200);
        qrImageView.setPreserveRatio(true);

        popupVBox.getChildren().addAll(titleLabel, qrImageView);

        Scene scene = new Scene(popupVBox, 250, 300);
        qrStage.setScene(scene);
        qrStage.showAndWait();
    }

    private void switchBackToAthletefront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Athletefront.fxml"));
            Parent root = loader.load();

            Athletefront controller = loader.getController();
            controller.initialize();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Welcome Athlete!");
            stage.setUserData(this);
            stage.show();
            Stage currentStage = (Stage) getBack.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Failed to open athlete front: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
