package com.model;

import java.math.BigDecimal;
import java.sql.Date;

public class SalesReport {
    private int saleId;
    private Date saleDate;
    private String userName;
    private String productName;
    private int quantity;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private String paymentMethod;

    public SalesReport(int saleId, Date saleDate, String userName, String productName, int quantity, BigDecimal subTotal, BigDecimal totalAmount, String paymentMethod) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.userName = userName;
        this.productName = productName;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

	public int getSaleId() {
		return saleId;
	}

	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
