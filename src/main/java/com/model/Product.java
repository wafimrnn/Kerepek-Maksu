package com.model;

import java.sql.Date;

public class Product {
    private int prodId;
    private String prodName;
    private double prodPrice;
    private int quantityStock;
    private int restockLevel;
    private Date expiryDate;
    private String imagePath;
    private String prodStatus; // "Active" or "Inactive" (indicates if the product is active)

    // Default constructor
    public Product() {
    }

    // Parameterized constructor
    public Product(int prodId, String prodName, double prodPrice, int quantityStock, int restockLevel, Date expiryDate, String imagePath, String prodStatus) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.quantityStock = quantityStock;
        this.restockLevel = restockLevel;
        this.expiryDate = expiryDate;
        this.imagePath = imagePath;
        this.prodStatus = prodStatus;
    }

    // Getters and Setters
    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(int quantityStock) {
        this.quantityStock = quantityStock;
    }

    public int getRestockLevel() {
        return restockLevel;
    }

    public void setRestockLevel(int restockLevel) {
        this.restockLevel = restockLevel;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    // toString() method to return a string representation of the object
    @Override
    public String toString() {
        return "Product{" +
                "prodId=" + prodId +
                ", prodName='" + prodName + '\'' +
                ", prodPrice=" + prodPrice +
                ", quantityStock=" + quantityStock +
                ", restockLevel=" + restockLevel +
                ", expiryDate=" + expiryDate +
                ", imagePath='" + imagePath + '\'' +
                ", prodStatus='" + prodStatus + '\'' +
                '}';
    }
}