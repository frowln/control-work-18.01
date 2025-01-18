package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
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

        // Создаем заголовок
        Label titleLabel = new Label("Добро пожаловать в Чат-бот!");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");

        // Текстовая область для сообщений
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setStyle("-fx-control-inner-background: #f0f8ff; -fx-font-size: 14px;");

        // Поле ввода команды
        inputField = new TextField();
        inputField.setPromptText("Введите команду...");
        inputField.setStyle("-fx-font-size: 14px;");
        inputField.setOnAction(e -> handleCommand(inputField.getText()));

        // Основной макет с отступами
        VBox layout = new VBox(10, titleLabel, chatArea, inputField);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #e6f7ff; -fx-border-color: #b3d9ff; -fx-border-width: 2px; -fx-border-radius: 10px;");

        // Устанавливаем минимальные размеры окна
        layout.setPrefSize(450, 350);

        // Сцена
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
