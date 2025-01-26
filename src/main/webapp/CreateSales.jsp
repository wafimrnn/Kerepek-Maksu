<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.ProductDAO" %>
<%@ page import="com.model.Product" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sales</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/createSales.css">
    <link rel="stylesheet" type="text/css" href="css/notification.css">
</head>
<body>
    <!-- Sidebar Section -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp" class="active">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp">Account</a>
        </div>
    </div>

    <!-- Head Bar Section -->
    <div class="head-bar">
        <div class="title">Sales</div>
        <div class="icons">
            <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <div class="main-content">
    
        <!-- Product List Section -->
		<div class="product-list">
		    <div class="header">
		        <h1>Create Sales</h1>
		    </div>
		    <div id="product-grid" class="product-grid">
		        <%
		            ProductDAO productDAO = new ProductDAO();
		            List<Product> productList = productDAO.getAllActiveProducts();
		            for (Product product : productList) {
		                String imagePath = product.getImagePath();
		                if (imagePath == null || imagePath.isEmpty()) {
		                    imagePath = "img/default-image.jpg"; // Default image path for products without an image
		                }
		        %>
		            <div class="product-item">
		                <img src="<%= imagePath %>" alt="<%= product.getProdName() %>">
		                <h4><%= product.getProdName() %></h4>
		                <p>Price: RM <%= product.getProdPrice() %></p>
		                <button class="add-to-order" 
		                        data-prodId="<%= product.getProdId() %>" 
		                        data-prodName="<%= product.getProdName() %>" 
		                        data-prodPrice="<%= product.getProdPrice() %>">
		                    Add to Order
		                </button>
		            </div>
		        <% 
		            }
		        %>
		    </div>
		</div>

        <!-- Order Calculation Section --> 
        <div class="order-calculation">
            <div class="order-details">
                <h3>Order Details</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Qty</th>
                            <th>Subtotal</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody id="order-items">
                        <!-- Dynamically added order items will appear here -->
                    </tbody>
                </table>
                <div class="totals">
                    <p>Subtotal: <span id="subtotal">RM 0</span></p>
                    <p><strong>Total: <span id="total">RM 0</span></strong></p>
                </div>
            </div>

            <div class="payment-method">
                <button onclick="togglePayment('cash')">Cash</button>
                <button onclick="togglePayment('qr')">QR Payment</button>
            </div>

            <div id="cash" class="payment-details">
                <h3>Cash Payment</h3>
                <input type="text" id="money-received" placeholder="Money Received" oninput="calculateChange()">
                <input type="hidden" id="total-amount" value="0">
                <input type="hidden" id="payment-method" value="cash">
                <input type="text" id="change" placeholder="Change" disabled>
                <button onclick="completeOrder()">Complete Order</button>
            </div>

            <div id="qr" class="payment-details">
                <h3>QR Payment</h3>
                <input type="hidden" id="total-amount" value="0">
                <input type="hidden" id="payment-method" value="qr">
                <button onclick="completeOrder()">Confirm Payment</button>
            </div>

            <div id="response-message" style="display: none; margin-top: 10px; font-size: 16px;">
                Order Completed Successfully!
            </div>
        </div>
        </div>
        <div id="notification-popup" style="display: none;">
	    <ul id="notification-list"></ul>
	</div>
    <script src="js/pos.js"></script>
    <script src="js/notification.js"></script>
</body>
</html>