package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    private TextArea chatArea;
    private TextField inputField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Чат-бот на JavaFX");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        inputField = new TextField();

        inputField.setOnAction(e -> handleCommand(inputField.getText()));

        VBox layout = new VBox(10, chatArea, inputField);
        layout.setPrefSize(400, 300);

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleCommand(String command) {
        if (command.isBlank()) return;

        chatArea.appendText("> " + command + "\n");
        String response = processCommand(command.trim());
        chatArea.appendText(response + "\n");
        inputField.clear();
    }

    private String processCommand(String command) {
        if (command.equalsIgnoreCase("list")) {
            return "Доступные команды: list, weather <город>, exchange <валюта>, quit";
        } else if (command.toLowerCase().startsWith("weather")) {
            String city = command.substring(7).trim();
            if (city.isEmpty()) {
                return "Введите название города. Пример: weather Москва";
            }
            return WeatherService.getWeather(city);
        } else if (command.toLowerCase().startsWith("exchange") || command.toLowerCase().startsWith("rate")) {
            String[] parts = command.split(" ");
            if (parts.length < 2) {
                return "Введите код валюты. Пример: exchange USD";
            }
            String currency = parts[1].trim();
            return ExchangeRateService.getExchangeRate(currency);
        } else if (command.equalsIgnoreCase("quit")) {
            chatArea.clear();
            return "Вы вернулись на главную страницу.";
        } else {
            return "Неизвестная команда. Введите 'list' для списка команд.";
        }
    }

}
