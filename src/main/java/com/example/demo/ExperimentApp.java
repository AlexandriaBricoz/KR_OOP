package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ExperimentApp extends Application {
    private List<Tab> pondTabs = new ArrayList<>();
    private List<TextField> pondSizeFields, pondDepthFields, fishTypeFields, fishPriceFields, foodPriceFields;

    private FishFarm fishFarm;
    private ContractDetails contractDetails;
    private Stage primaryStage;

    private TextField contractDurationField, weeklyFeedCostField;
    private TextArea reportTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            primaryStage.setTitle("Эксперимент с рыбоводством");

            TabPane tabPane = new TabPane();

            pondSizeFields = new ArrayList<>();
            pondDepthFields = new ArrayList<>();
            fishTypeFields = new ArrayList<>();
            fishPriceFields = new ArrayList<>();
            foodPriceFields = new ArrayList<>();

            contractDurationField = new TextField();
            weeklyFeedCostField = new TextField();

            Button addPondButton = new Button("Добавить пруд");
            addPondButton.setOnAction(e -> addPondFields());

            Button initButton = new Button("Инициализировать эксперимент");
            initButton.setOnAction(e -> {
                initializeExperiment();
                fishFarm.simulateContractPeriod();
                checkProfitability();
            });

            VBox buttonsVBox = new VBox(10);
            buttonsVBox.getChildren().addAll(addPondButton, initButton);

            reportTextArea = new TextArea();
            reportTextArea.setEditable(false);
            reportTextArea.setWrapText(true);

            Tab reportTab = new Tab("Отчет");
            reportTab.setContent(reportTextArea);

            VBox root = new VBox(10);
            root.getChildren().addAll(tabPane, buttonsVBox);

            Scene scene = new Scene(root, 400, 500);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Добавление вкладки "Контракт"
            Tab contractTab = addContractFields();
            tabPane.getTabs().add(contractTab);

            // Добавление вкладки "Отчет"
            tabPane.getTabs().add(reportTab);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private Tab addContractFields() {

        GridPane contractGrid = new GridPane();
        contractGrid.setVgap(10);

        // Добавление текстовых полей для ввода данных о контракте
        contractGrid.addRow(0, new Label("Длительность контракта:"), contractDurationField);
        contractGrid.addRow(1, new Label("Еженедельная стоимость корма:"), weeklyFeedCostField);

        // Добавление вкладки "Контракт"
        Tab contractTab = new Tab("Контракт");
        contractTab.setContent(contractGrid);
        pondTabs.add(contractTab);

        return contractTab;
    }

    private void addPondFields() {
        int pondIndex = pondTabs.size() + 1;

        // Создание текстовых полей для ввода данных о пруде
        TextField pondSizeField = new TextField();
        TextField pondDepthField = new TextField();
        TextField fishTypeField = new TextField();
        TextField fishPriceField = new TextField();
        TextField foodPriceField = new TextField();

        pondSizeFields.add(pondSizeField);
        pondDepthFields.add(pondDepthField);
        fishTypeFields.add(fishTypeField);
        fishPriceFields.add(fishPriceField);
        foodPriceFields.add(foodPriceField);

        GridPane pondGrid = new GridPane();
        pondGrid.setVgap(10);

        // Добавление текстовых полей для ввода данных о пруде на вкладке "Прод"
        pondGrid.addRow(0, new Label("Размер пруда:"), pondSizeField);
        pondGrid.addRow(1, new Label("Глубина пруда:"), pondDepthField);
        pondGrid.addRow(2, new Label("Тип рыбы:"), fishTypeField);
        pondGrid.addRow(3, new Label("Стоимость рыбы:"), fishPriceField);
        pondGrid.addRow(4, new Label("Стоимость корма:"), foodPriceField);

        // Добавление полей контракта для каждого нового пруда
//        pondGrid.addRow(5, new Label("Длительность контракта:"), contractDurationField);
//        pondGrid.addRow(6, new Label("Еженедельная стоимость корма:"), weeklyFeedCostField);

        Tab newTab = new Tab("Пруд " + (pondIndex - 1));
        newTab.setContent(pondGrid);
        pondTabs.add(newTab);

        TabPane tabPane = (TabPane) ((VBox) ((Scene) primaryStage.getScene()).getRoot()).getChildren().get(0);
        tabPane.getTabs().add(newTab);
    }

    private void initializeExperiment() {
        try {
            int contractDuration = Integer.parseInt(contractDurationField.getText());
            double weeklyFeedCost = Double.parseDouble(weeklyFeedCostField.getText());

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

    private void checkProfitability() {
        try {
            double totalCapital = fishFarm.calculateTotalCapital();
            double totalFeedCost = contractDetails.getWeeklyFeedCost() * contractDetails.getDuration();
            double totalFishValue = fishFarm.calculateTotalFishValueAdult() + fishFarm.calculateTotalFishValueYoung();
            double profit = totalFishValue - totalFeedCost;

            reportTextArea.setText(
                    "Общий капитал: " + Math.round(totalCapital) + "\n" +
                            "Общая стоимость корма: " + Math.round(totalFeedCost) + "\n" +
                            "Общая стоимость рыбы: " + Math.round(totalFishValue) + "\n" +
                            "Прибыль: " + profit + "\n" +
                            ((profit >= 0) ? "Рентабельность: Контракт прибыльный!" : "Рентабельность: Контракт не прибыльный.")
            );

        } catch (NullPointerException ex) {
            System.out.println("Пожалуйста, сначала инициализируйте эксперимент.");
        }
    }
}
