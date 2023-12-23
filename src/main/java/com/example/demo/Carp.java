package com.example.demo;

public class Carp extends Fish {
    private String color; // Дополнительное свойство для карпа

    public Carp(String type, int price, String color) {
        super(type, price);
        this.color = color;
    }

    // Геттер для цвета
    public String getColor() {
        return color;
    }

    // Сеттер для цвета
    public void setColor(String color) {
        this.color = color;
    }

    // Можно добавить дополнительные методы, специфичные для карпа, если это необходимо
}
