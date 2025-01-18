package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.*;
import java.net.URI;

public class WeatherService {
    private static final String API_KEY = "bd5e378503939ddaee76f12ad7a97608";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static String getWeather(String city) {
        try {
            String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=ru";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(response.body());
                String weather = json.get("weather").get(0).get("description").asText();
                double temp = json.get("main").get("temp").asDouble();
                return String.format("Погода в %s: %.1f°C, %s.", city, temp, weather);
            } else {
                return "Ошибка: город не найден.";
            }
        } catch (Exception e) {
            return "Ошибка при получении данных о погоде.";
        }
    }
}

