package com.model;

public class SaleItem {
    private int saleId; // References SALE_ID in the sale_items table
    private int productId; // References PROD_ID in the sale_items table
    private int quantity; // Quantity of the product sold
    private double subtotal; // Subtotal for this product in the sale

    // Getters and Setters
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}