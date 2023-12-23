package com.example.demo;

class Fish {
    private String type;
    private int price;

    public Fish(String type, int price) {
        this.type = type;
        this.price = price;
    }

    // getters
    public String getType() {
        return type;
    }


    // setters (optional, depending on your needs)
    public void setType(String type) {
        this.type = type;
    }

    // getters
    public int getPrice() {
        return price;
    }


    // setters (optional, depending on your needs)
    public void setPrice(int price) {
        this.price = price;
    }

}
