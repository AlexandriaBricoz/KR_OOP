package com.example.demo;

import java.util.HashMap;
import java.util.Map;

class Pond {
    private Map<String, Double> fishPopulation;
    private int cleaningPeriod;
    private int weeksSinceCleaning;
    private int price;
    private String fishType;
    private int foodPrice;

    public String getFishType() {
        return fishType;
    }

    public int getPrice() {
        return price;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public Pond(double initialFishPopulation, int cleaningPeriod, String fishType, int price, int foodPrice) {
        this.fishPopulation = new HashMap<>();
        this.fishPopulation.put("young", initialFishPopulation);
        this.fishPopulation.put("adult", 0.0);
        this.cleaningPeriod = cleaningPeriod;
        this.weeksSinceCleaning = 0;
        this.fishType = fishType;
        this.price = price;
        this.foodPrice = foodPrice;
    }

    public void performWeeklyCycle() {
        double alpha, beta, delta;
        double youngFish = fishPopulation.get("young");
        double adultFish = fishPopulation.get("adult");


        alpha = 1.5;
        beta = 0.6;
        delta = 0.12;

        double newYoungFish = alpha * youngFish;
        double newAdultFish = beta * youngFish - delta * (adultFish + youngFish);

        fishPopulation.put("young", newYoungFish);
        fishPopulation.put("adult", newAdultFish);

        weeksSinceCleaning++;

        if (weeksSinceCleaning == cleaningPeriod) {
            cleanPond();
        }
    }

    private void cleanPond() {
        this.fishPopulation.put("young", 0.0);
        this.fishPopulation.put("adult", 0.0);
        weeksSinceCleaning = 0;
    }

    public double getFishPopulation(String category) {
        return fishPopulation.get(category);
    }

    public void setFishPopulation(String category, double value) {
        this.fishPopulation.put(category, value);
    }
}