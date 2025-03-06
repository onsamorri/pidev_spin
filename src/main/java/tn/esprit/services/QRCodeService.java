package tn.esprit.services;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import tn.esprit.entities.TrainingSession;

public class QRCodeService {
    private static final String QR_API_URL = "https://quickchart.io/qr?text=";

    public String generateQRCode(TrainingSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Training session cannot be null");
        }

        // Convert training session details into a vCard format
        String vCardData = String.format(
                "BEGIN:VCARD\n" +
                        "VERSION:3.0\n" +
                        "FN:%s\n" +
                        "ORG:%s\n" +
                        "TITLE:%s\n" +
                        "TEL:%s\n" +
                        "EMAIL:%s\n" +
                        "ADR:%s\n" +
                        "END:VCARD",
                session.getFocus().name(),
                "Training Session",
                session.getStart_time().toString(),
                session.getDuration().toString(),
                session.getLocation(),
                "Location: " + session.getLocation()
        );

        // Encode the vCard string properly
        String encodedData = URLEncoder.encode(vCardData, StandardCharsets.UTF_8);

        // Generate QR code URL
        String apiUrl = QR_API_URL + encodedData;

        System.out.println("QR Code URL: " + apiUrl);

        // Send HTTP request to validate the QR code URL
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                return apiUrl;
            } else {
                throw new RuntimeException("Failed to generate QR Code, HTTP Status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}