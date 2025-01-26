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
    <style>
        /* General Styling */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            display: flex;
            min-height: 100vh;
            background-image: url('img/pisangImage.jpg'); /* Background image */
            background-size: cover; /* Cover the entire area */
            background-position: center; /* Center the image */
            position: relative;
        }

        /* Sidebar Styling */
        .sidebar {
            width: 220px;
            background-color: #481D01; /* Semi-transparent background color */
            color: white;
            display: flex;
            flex-direction: column;
            padding: 20px;
            backdrop-filter: blur(10px); /* Apply blur effect to the background */
        }

        .sidebar h2 {
            margin-bottom: 20px;
            text-align: center;
            font-size: 20px;
            border-bottom: 1px solid #495057;
            padding-bottom: 10px;
        }

        .nav-links {
            display: flex;
            flex-direction: column;
        }

        .nav-links a {
            text-decoration: none;
            color: white; /* Default font color */
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 4px;
            transition: background 0.3s ease;
        }

        .nav-links a:hover {
            background-color: #FEECC3;
            color: black;
        }

        .nav-links a.active {
            background-color: #F6C324; /* Background color for active link */
            color: black; /* Font color for active link */
        }
        
        /* Head Bar Styling */
        .head-bar {
            width: calc(100% - 220px); /* Full width minus the sidebar width */
            height: 60px;
            background-color: #F6C324; /* Semi-transparent background color */
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
            position: fixed;
            top: 0;
            left: 220px; /* Push the head bar right to align with the sidebar */
            z-index: 1000; /* Ensure it stays on top */
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px); /* Apply blur effect to the background */
        }
        
        .head-bar .title {
            font-size: 20px;
            font-weight: bold;
            color: black;
        }
        
        .head-bar .icons {
            display: flex;
            align-items: center;
            gap: 15px;
            color: #ffffff;
        }
        
        .head-bar .icons i {
            font-size: 20px;
            cursor: pointer;
            transition: color 0.3s ease;
        }
        
        .head-bar .icons i:hover {
            color: #ddd;
        }

        /* Main Content Styling */
		.main-content {
            flex: 1;
            padding: 20px;
            position: relative; /* Position relative for the overlay */
            display: flex;
            flex-direction: column;
            align-items: right;
            justify-content: flex-start;
            margin-top: 60px; /* Push content below the head bar */
            overflow: hidden; /* Prevent overflow */
        }
		
		.main-content::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-image: url('img/pisangImage.jpg'); /* Path to your image */
            background-size: cover; /* Cover the entire area */
            background-position: center; /* Center the image */
            background-repeat: no-repeat; /* Prevent the image from repeating */
            
            filter: blur(1px); /* Adjust blur intensity */
            z-index: -1; /* Push background below all content */
        }
        
       .main-content h1 {
            font-size: 28px;
            color: white;
            margin-bottom: 20px;
        }
		
		/* Product List Styling */
		.product-list {
		    flex: 2;
		    margin-right: 20px;
		}
		
		.product-list h3 {
		    margin-bottom: 10px;
		}
		
		.product-grid {
		    display: grid;
		    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
		    gap: 10px;
		}
		
		.product-item {
		    background-color: #fff;
		    padding: 10px;
		    border-radius: 5px;
		    text-align: center;
		    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
		}
		
		.product-item img {
		    width: 100%;
		    height: 100px;
		    object-fit: cover;
		    border-radius: 5px;
		    margin-bottom: 10px;
		}
		
		.product-item button {
		    background-color: #FAD02C;
		    color: white;
		    border: none;
		    padding: 5px 10px;
		    border-radius: 3px;
		    cursor: pointer;
		}
		
		.product-item button:hover {
		    background-color: #F6C324;
		    color: black;
		}
		
		/* Order Calculation Section */
		.order-calculation {
			position: absolute;
			top:80px;
			right: 20px;
		    flex: 1;
		    background-color: #fff;
		    padding: 20px;
		    border-radius: 5px;
		    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
		    max-width: 400px;
		    height: fit-content; /* Makes sure the height fits its content */
		}
		
		/* Order Details Styling */
        .order-details table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            table-layout: fixed; /* Prevent table from overflowing */
        }

        .order-details table th, .order-details table td {
            text-align: left;
            padding: 8px;
            border-bottom: 1px solid #ddd;
            font-size: 14px; /* Smaller font size for compact layout */
            word-wrap: break-word; /* Allow text wrapping */
        }

        .order-details table th.qty, .order-details table td.qty {
            width: 50px; /* Adjusted smaller width for quantity */
            text-align: center;
        }

        .order-details table th.action, .order-details table td.action {
            width: 60px; /* Adjusted width for action buttons */
            text-align: center;
        }

        .order-details input {
            width: 40px; /* Smaller fixed width for quantity input */
            text-align: center;
            font-size: 14px;
            padding: 2px;
        }

        .order-details button {
            padding: 2px 6px; /* Smaller buttons for compact design */
            font-size: 12px;
            background-color: #FAD02C;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        .order-details button:hover {
            background-color: #F6C324;
            color: black;
        }
		
		.totals {
		    margin-bottom: 15px;
		    font-size: 14px; /* Smaller font for totals */
		    display: flex;
		    justify-content: space-between;
		}
		
		.payment-method {
		    margin-bottom: 15px;
		}
		
		.payment-method button {
		    width: 48%;
		    padding: 10px;
		    margin-right: 2%;
		    background-color: #FAD02C;
		    color: black;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		}
		
		.payment-method button:hover {
		    background-color: #F6C324;
		    color: black;
		}
		
		.payment-method button:last-child {
		    margin-right: 0;
		}
		
		.payment-details {
		    display: none;
		}
		
		.payment-details.active {
		    display: block;
		}
		
		.payment-details input {
		    width: 100%;
		    padding: 10px;
		    border: 1px solid #ddd;
		    border-radius: 5px;
		    margin-bottom: 10px;
		}
		
		.payment-details button {
		    width: 100%;
		    padding: 10px;
		    background-color: #FAD02C;
		    color: black;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		}
		
		.payment-details button:hover {
		    background-color: #F6C324;
		    color: black;
		}
    </style>
</head>
<body>
    <!-- Sidebar Section -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProduct.jsp">Product</a>
            <a href="CreateSales.jsp" class="active">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp">Account</a>
        </div>
    </div>

    <!-- Head Bar Section -->
    <div class="head-bar">
        <div class="title">Sales</div>
        <div class="icons">
            <i class="fas fa-bell" title="Notifications"></i>
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
                %>
                <div class="product-item">
                    <img src="<%= product.getImagePath()%>" alt="<%= product.getProdName() %>">
                    <h4><%= product.getProdName() %></h4>
                    <p>Price: RM <%= product.getProdPrice() %></p>
                    <button class="add-to-order" 
                            data-prodId="<%= product.getProdId() %>" 
                            data-prodName="<%= product.getProdName() %>" 
                            data-prodPrice="<%= product.getProdPrice() %>">
                        Add to Order
                    </button>
                </div>
                <% } %>
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
   

    <script src="pos.js"></script>
</body>
</html>