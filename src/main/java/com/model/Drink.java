package com.model;

import java.sql.Date;

public class Drink extends Product {
    private Double volume; // Volume of the drink (e.g., in liters or milliliters)

    // Default constructor
    public Drink() {
        super(); // Calling the constructor of the superclass (Product)
    }

    // Parameterized constructor
    public Drink(int prodId, String prodName, double prodPrice, int quantityStock, int restockLevel, Date expiryDate, String imagePath, String prodStatus, double volume) {
        super(prodId, prodName, prodPrice, quantityStock, restockLevel, expiryDate, imagePath, prodStatus);
        this.volume = volume;
    }

    // Getter and Setter
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    // toString() method to return a string representation of the object
    public String toString() {
        return "Drink{" +
                "volume=" + volume +
                ", " + super.toString() + // calling toString() from Product class
                '}';
    }
}