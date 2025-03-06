package tn.esprit.services;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {
    private static final String API_URL = "https://api.weatherapi.com/v1/current.json?key=";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public WeatherService(String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.apiKey = apiKey;
    }

    public String getWeather(String location) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL + apiKey + "&q=" + location))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.body());

                if (jsonNode.has("error")) {  // Handle invalid city error
                    return "City not found. Please check the name.";
                }

                String temp = jsonNode.get("current").get("temp_c").asText() + "°C";
                String humidity = jsonNode.get("current").get("humidity").asText() + "%";
                String windSpeed = jsonNode.get("current").get("wind_kph").asText() + " km/h";
                String condition = jsonNode.get("current").get("condition").get("text").asText();

                return String.format("Temp: %s\nHumidity: %s\nWind: %s\nCondition: %s", temp, humidity, windSpeed, condition);
            } else {
                return "Error fetching weather data.";
            }
        } catch (Exception e) {
            return "Failed to fetch weather: " + e.getMessage();
        }
    }
}
