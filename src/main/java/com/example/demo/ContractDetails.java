package com.example.demo;

public class ContractDetails {
    private int duration;
    private double weeklyFeedCost;

    public ContractDetails(int duration, double weeklyFeedCost) {
        this.duration = duration;
        this.weeklyFeedCost = weeklyFeedCost;
    }


    public double getWeeklyFeedCost() {
        return weeklyFeedCost;
    }

    public int getDuration() {
        return duration;
    }
}
