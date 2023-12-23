package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ExperimentApp extends Application {
    private FishFarm fishFarm;
    private ContractDetails contractDetails;
    private Stage primaryStage;
    private TextField contractDurationField, weeklyFeedCostField, contactNameField, contactEmailField, contactPhoneField;
    private List<TextField> pondSizeFields, pondDepthFields, fishTypeFields, fishPriceFields, foodPriceFields;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Эксперимент с рыбоводством");

        // Инициализация текстовых полей ввода
        contractDurationField = new TextField();
        weeklyFeedCostField = new TextField();
        contactNameField = new TextField();
        contactEmailField = new TextField();
        contactPhoneField = new TextField();

        // Инициализация списков для полей ввода о прудах
        pondSizeFields = new ArrayList<>();
        pondDepthFields = new ArrayList<>();
        fishTypeFields = new ArrayList<>();
        fishPriceFields = new ArrayList<>();
        foodPriceFields = new ArrayList<>();

        // Инициализация кнопок
        Button addPondButton = new Button("Добавить пруд");
        addPondButton.setOnAction(e -> addPondFields());

        Button initButton = new Button("Инициализировать эксперимент");
        initButton.setOnAction(e -> {
            initializeExperiment();
            fishFarm.simulateContractPeriod();
            checkProfitability();
        });

        // Настройка компоновки элементов
        VBox root = new VBox(10);
        root.getChildren().addAll(createInputFields(), addPondButton, initButton, createResultLabels());

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Метод для добавления полей пруда
    private void addPondFields() {
        Label pondLabel = new Label("Пруд " + (pondSizeFields.size() + 1));

        TextField pondSizeField = new TextField();
        TextField pondDepthField = new TextField();
        TextField fishTypeField = new TextField();
        TextField fishPriceField = new TextField();
        TextField foodPriceField = new TextField();

        // Добавление полей в соответствующие списки
        pondSizeFields.add(pondSizeField);
        pondDepthFields.add(pondDepthField);
        fishTypeFields.add(fishTypeField);
        fishPriceFields.add(fishPriceField);
        foodPriceFields.add(foodPriceField);

        VBox pondFields = new VBox(5);
        pondFields.getChildren().addAll(
                pondLabel,
                new Label("Размер пруда:"), pondSizeField,
                new Label("Глубина пруда:"), pondDepthField,
                new Label("Тип рыбы:"), fishTypeField,
                new Label("Стоимость рыбы:"), fishPriceField,
                new Label("Стоимость корма:"), foodPriceField
        );

        // Добавление новых полей на сцену
        ((VBox) primaryStage.getScene().getRoot()).getChildren().add(((VBox) primaryStage.getScene().getRoot()).getChildren().size() - 1, pondFields);
    }

    // Метод для создания полей ввода
    private VBox createInputFields() {
        Label durationLabel = new Label("Длительность контракта:");
        Label weeklyFeedCostLabel = new Label("Еженедельная стоимость корма:");
        Label contactNameLabel = new Label("Имя контактного лица:");
        Label contactEmailLabel = new Label("Email контактного лица:");
        Label contactPhoneLabel = new Label("Телефон контактного лица:");

        VBox inputFields = new VBox(10);
        inputFields.getChildren().addAll(
                durationLabel, contractDurationField,
                weeklyFeedCostLabel, weeklyFeedCostField,
                contactNameLabel, contactNameField,
                contactEmailLabel, contactEmailField,
                contactPhoneLabel, contactPhoneField
        );

        // Добавление полей для каждого пруда
        // Добавление полей для каждого пруда
        int pondIndex = 0;
        while (pondIndex < pondSizeFields.size()) {
            Label pondLabel = new Label("Пруд " + (pondIndex / 5 + 1));
            inputFields.getChildren().addAll(
                    pondLabel,
                    new Label("Размер пруда:"), pondSizeFields.get(pondIndex),
                    new Label("Глубина пруда:"), pondDepthFields.get(pondIndex),
                    new Label("Тип рыбы:"), fishTypeFields.get(pondIndex),
                    new Label("Стоимость рыбы:"), fishPriceFields.get(pondIndex),
                    new Label("Стоимость корма:"), foodPriceFields.get(pondIndex)
            );
            pondIndex++;
        }

        return inputFields;
    }

    // Метод для инициализации эксперимента
    private void initializeExperiment() {
        try {
            int contractDuration = Integer.parseInt(contractDurationField.getText());
            double weeklyFeedCost = Double.parseDouble(weeklyFeedCostField.getText());

            // Проверка, что все поля прудов заполнены
            if (pondSizeFields.isEmpty() || pondDepthFields.isEmpty() || fishTypeFields.isEmpty() || fishPriceFields.isEmpty() || foodPriceFields.isEmpty()) {
                System.out.println("Пожалуйста, добавьте информацию о прудах.");
                return;
            }

            // Создание деталей контракта
            contractDetails = new ContractDetails(contractDuration, weeklyFeedCost);
            // Создание списка прудов на основе введенных данных
            List<Pond> fishPonds = createPondsFromInput();

            // Создание фирмы и рыбоводческой фермы
            Firm firm = new Firm("Google", 200);
            fishFarm = new FishFarm(560000, contractDuration, contractDetails, fishPonds);

            System.out.println("Эксперимент успешно инициализирован");
        } catch (NumberFormatException ex) {
            System.out.println("Пожалуйста, введите корректные числовые значения для длительности контракта и еженедельной стоимости корма.");
        }
    }
    // Метод для создания списка прудов из введенных данных
    private List<Pond> createPondsFromInput() {
        List<Pond> ponds = new ArrayList<>();

        for (int i = 0; i < pondSizeFields.size(); i++) {
            double pondSize = Double.parseDouble(pondSizeFields.get(i).getText());
            int pondDepth = Integer.parseInt(pondDepthFields.get(i).getText());
            String fishType = fishTypeFields.get(i).getText();
            double fishPrice = Double.parseDouble(fishPriceFields.get(i).getText());
            double foodPrice = Double.parseDouble(foodPriceFields.get(i).getText());

            Pond pond = new Pond(pondSize, pondDepth, fishType, (int) fishPrice, (int) foodPrice);
            ponds.add(pond);
        }

        return ponds;
    }

    // Метод для создания меток с результатами
    private VBox createResultLabels() {
        Label totalCapitalLabel = new Label("Общий капитал:");
        Label totalFeedCostLabel = new Label("Общая стоимость корма:");
        Label totalFishValueLabel = new Label("Общая стоимость рыбы:");
        Label profitLabel = new Label("Прибыль:");
        Label profitabilityLabel = new Label("Рентабельность:");

        VBox resultLabels = new VBox(10, totalCapitalLabel, totalFeedCostLabel, totalFishValueLabel, profitLabel, profitabilityLabel);

        totalCapitalLabel.setStyle("-fx-font-weight: bold;");
        totalFeedCostLabel.setStyle("-fx-font-weight: bold;");
        totalFishValueLabel.setStyle("-fx-font-weight: bold;");
        profitLabel.setStyle("-fx-font-weight: bold;");
        profitabilityLabel.setStyle("-fx-font-weight: bold;");

        return resultLabels;
    }

    // Метод для проверки рентабельности
    private void checkProfitability() {
        try {
            double totalCapital = fishFarm.calculateTotalCapital();
            double totalFeedCost = contractDetails.getWeeklyFeedCost() * contractDetails.getDuration();
            double totalFishValue = fishFarm.calculateTotalFishValueAdult() + fishFarm.calculateTotalFishValueYoung();
            double profit = totalFishValue - totalFeedCost;

            VBox resultLabels = (VBox) ((VBox) primaryStage.getScene().getRoot()).getChildren().get(((VBox) primaryStage.getScene().getRoot()).getChildren().size() - 1);
            ((Label) resultLabels.getChildren().get(0)).setText("Общий капитал: " + totalCapital);
            ((Label) resultLabels.getChildren().get(1)).setText("Общая стоимость корма: " + totalFeedCost);
            ((Label) resultLabels.getChildren().get(2)).setText("Общая стоимость рыбы: " + totalFishValue);
            ((Label) resultLabels.getChildren().get(3)).setText("Прибыль: " + profit);

            String profitabilityText = (profit >= 0) ? "Рентабельность: Контракт прибыльный!" : "Рентабельность: Контракт не прибыльный.";
            ((Label) resultLabels.getChildren().get(4)).setText(profitabilityText);
        } catch (NullPointerException ex) {
            System.out.println("Пожалуйста, сначала инициализируйте эксперимент.");
        }
    }
}
