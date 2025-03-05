package tn.esprit.services;

import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.*;

public class ExerciseServices {

    private static final String API_URL = "https://wger.de/api/v2/exercise/";
    private static final String API_KEY = "3f9f8c3147a144212d971fadff011621ae419fa5";

    // Fetch exercise details based on the exercise name
    public static String fetchExerciseDetailsFromAPI(String exerciseName) {
        try {

            String encodedExerciseName = URLEncoder.encode(exerciseName, "UTF-8");


            URL url = new URL(API_URL + "?name=" + encodedExerciseName);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            connection.setRequestProperty("Authorization", "Token " + API_KEY);
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // If the response is successful (HTTP 200 OK), read the input stream
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString(); // Return the detailed exercise information as a String
            } else {
                System.out.println("Error: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Fetch exercise data from the Wger API
    public static String fetchExerciseData() {
        try {
            // Construct the URL for fetching exercise data (for example, all exercises)
            URL url = new URL(API_URL + "?language=2");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set the Authorization header with the API key
            connection.setRequestProperty("Authorization", "Token " + API_KEY);
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // If the response is successful (HTTP 200 OK), read the input stream
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                System.out.println("Error: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Parse exercise data and return a list of exercise names
    public static List<String> parseExerciseData(String response) {
        List<String> exercises = new ArrayList<>();
        if (response == null || response.isEmpty()) {
            System.out.println("No data received");
            return exercises;
        }

        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(response).getAsJsonObject();
            JsonArray exerciseArray = jsonResponse.getAsJsonArray("results");
            for (JsonElement exerciseElement : exerciseArray) {
                String exerciseName = exerciseElement.getAsJsonObject().get("name").getAsString();
                exercises.add(exerciseName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exercises;
    }

    // Parse detailed exercise response
    public static String parseExerciseDetails(String response) {
        if (response == null || response.isEmpty()) {
            return "No details available.";
        }

        try {
            // You can expand this with actual JSON parsing to extract description or other details
            String description = "This exercise boosts strength, endurance, and flexibility. Maintain good posture, engage your core, and control your movements.\""; // Placeholder for now.

            return description;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing exercise details.";
        }
    }
}