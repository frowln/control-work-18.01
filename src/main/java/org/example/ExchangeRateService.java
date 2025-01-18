package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRateService {
    private static final String API_KEY = "02c622f203fe44bbb17a3c09fa686187";
    private static final String BASE_URL = "https://openexchangerates.org/api/latest.json?app_id=%s";

    public static String getExchangeRate(String currency) {
        try {
            String url = String.format(BASE_URL, API_KEY);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(response.body());

                JsonNode rates = json.get("rates");
                if (rates != null) {
                    if (!rates.has("RUB")) {
                        return "Ошибка: курс RUB отсутствует в данных.";
                    }
                    double rubRate = rates.get("RUB").asDouble();

                    if (rates.has(currency.toUpperCase())) {
                        double targetRate = rates.get(currency.toUpperCase()).asDouble();
                        double rateToRub = rubRate / targetRate;
                        return String.format("Курс %s к рублю: %.2f.", currency.toUpperCase(), rateToRub);
                    } else {
                        return "Ошибка: валюта не найдена.";
                    }
                } else {
                    return "Ошибка: не удалось получить курсы валют.";
                }
            } else {
                return "Ошибка: сервис курсов валют недоступен.";
            }
        } catch (Exception e) {
            return "Ошибка при подключении к API курса валют.";
        }
    }
}


