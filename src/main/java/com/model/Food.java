package com.model;

import java.sql.Date;

public class Food extends Product {
    private String packagingType;
    private Double weight;

    // Default constructor
    public Food() {
        super(); // Calling the constructor of the superclass (Product)
    }

    // Parameterized constructor
    public Food(int prodId, String prodName, double prodPrice, int quantityStock, int restockLevel, Date expiryDate, String imagePath, String prodStatus, String packagingType, double weight) {
        super(prodId, prodName, prodPrice, quantityStock, restockLevel, expiryDate, imagePath, prodStatus);
        this.packagingType = packagingType;
        this.weight = weight;
    }

    // Getters and Setters
    public String getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(String packagingType) {
        this.packagingType = packagingType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // toString() method to return a string representation of the object
    public String toString() {
        return "Food{" +
                "packagingType='" + packagingType + '\'' +
                ", weight=" + weight +
                ", " + super.toString() + // calling toString() from Product class
                '}';
    }
}
