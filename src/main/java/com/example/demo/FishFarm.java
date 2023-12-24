package com.example.demo;

import java.util.List;

public class FishFarm {
    private double capital;
    private int contractDuration;
    private ContractDetails contractDetails;
    private Pond[] fishPonds;

    public FishFarm(double initialCapital, int contractDuration, ContractDetails contractDetails, List<Pond> fishPonds) {
        this.capital = initialCapital;
        this.contractDuration = contractDuration;
        this.fishPonds = fishPonds.toArray(new Pond[0]);
        this.contractDetails = contractDetails;
    }

    private void handleContractDetails(int foodPrice, int price, Pond pond) {
        double fishValue = calculateTotalFishValueAdult();
        capital += fishValue * price;
        System.out.println("Sold fish.");
        fishValue = calculateTotalFishValueYoung();
        if (foodPrice * fishValue <= capital) {
            capital -= foodPrice * fishValue;
            System.out.println("Bought food for the fish.");
        } else {
            System.out.println("Not enough money to buy food for the fish.");
            double youngFish = pond.getFishPopulation("young");
            double newYoungFish = youngFish * (capital / foodPrice * fishValue);
            pond.setFishPopulation("young", newYoungFish);
            capital = 0;
        }
    }

    private void printWeeklyReport(int week) {
        System.out.println("===== Weekly Report - Week " + (week + 1) + " =====");
        ExperimentApp.appendTextToTextArea("===== Weekly Report - Week " + (week + 1) + " =====");
        for (int i = 0; i < fishPonds.length; i++) {
            Pond pond = fishPonds[i];
            System.out.println("Pond " + (i + 1) + ": Young Fish - " + Math.round(pond.getFishPopulation("young")) +
                    ", Adult Fish - " + Math.round(pond.getFishPopulation("adult")) +
                    ", Fish Type - " + pond.getFishType());
            ExperimentApp.appendTextToTextArea("Pond " + (i + 1) + ": Young Fish - " + Math.round(pond.getFishPopulation("young")) +
                    ", Adult Fish - " + Math.round(pond.getFishPopulation("adult")) +
                    ", Fish Type - " + pond.getFishType());
            pond.setFishPopulation("adult", 0);
        }
        double roundedTotalCapital = Math.round(calculateTotalCapital() * 100.0) / 100.0;
        System.out.println("Total Capital at the end of the contract: " + roundedTotalCapital);
        ExperimentApp.appendTextToTextArea("Total Capital at the end of the contract: " + roundedTotalCapital);
        System.out.println();
    }

    double calculateTotalCapital() {
        double totalFishValue = calculateTotalFishValueAdult() + calculateTotalFishValueYoung();
        return capital + totalFishValue;
    }

    double calculateTotalFishValueYoung() {
        double totalFishValue = 0;


        for (Pond pond : fishPonds) {
            double pondFishValue = (Double.isNaN(pond.getFishPopulation("young")) ? 0.0 : pond.getFishPopulation("young"));
            totalFishValue += pondFishValue;
        }


        return totalFishValue;
    }
    double calculateTotalFishValueYoungPrice() {
        double totalFishValue = 0;


        for (Pond pond : fishPonds) {
            double pondFishValue = (Double.isNaN(pond.getFishPopulation("young")) ? 0.0 : pond.getFishPopulation("young"));
            totalFishValue += (pondFishValue) * pond.getPrice();
        }


        return totalFishValue;
    }

    double calculateTotalFishValueAdult() {
        double totalFishValue = 0;

        for (Pond pond : fishPonds) {

            // Checking for None and setting default value

            double pondFishValue = Double.isNaN(pond.getFishPopulation("adult")) ? 0.0 : pond.getFishPopulation("adult");
            totalFishValue += pondFishValue;

        }


        return totalFishValue;
    }
    double calculateTotalFishValueAdultPrice() {
        double totalFishValue = 0;

        double summ = 0;
        for (Pond pond : fishPonds) {

            // Checking for None and setting default value

            summ = summ + (pond.getAdultFish()) * pond.getPrice();

        }


        return summ;
    }


    public void simulateContractPeriod() {
        for (int week = 0; week < contractDuration; week++) {
            for (Pond pond : fishPonds) {
                pond.performWeeklyCycle();
                handleContractDetails(pond.getFoodPrice(), pond.getPrice(), pond);
            }
            printWeeklyReport(week);
        }
    }

}
