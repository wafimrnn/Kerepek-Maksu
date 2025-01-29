<%@ page import="com.model.Sale" %>
<%@ page import="com.dao.SaleDAO" %>
<%@ page import="com.model.Product" %>
<%@ page import="com.dao.ProductDAO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
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
            background-image: url('img/pisangImage.jpg');
            background-size: cover;
            background-position: center;
            position: relative;
        }

        /* Sidebar Styling */
        .sidebar {
            width: 220px;
            background-color: #481D01;
            color: white;
            display: flex;
            flex-direction: column;
            padding: 20px;
            backdrop-filter: blur(10px);
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
            color: white;
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
            background-color: #F6C324;
            color: black;
        }

        /* Head Bar Styling */
        .head-bar {
            width: calc(100% - 220px);
            height: 60px;
            background-color: #F6C324;
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
            position: fixed;
            top: 0;
            left: 220px;
            z-index: 1000;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
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

        /* Main Content Area */
        .main-content {
            flex: 1;
            padding: 20px;
            position: relative;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            margin-top: 60px;
            overflow: hidden;
        }

        .section {
            width: 90%;
            max-width: 800px;
            background: #FBE39D;
            padding: 20px;
            margin-bottom: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .section h2 {
            font-size: 24px;
            margin-bottom: 15px;
            color: #481D01;
        }

        /* Table Styling */
        .section table {
            width: 100%;
            border-collapse: collapse;
        }

        .section table th,
        .section table td {
            padding: 12px 15px;
            text-align: left;
            border: 1px solid #ddd;
        }

        .section table th {
            background-color: #F6C324;
            color: white;
            font-weight: bold;
        }

        .section table td {
            background-color: #FBE39D;
        }

        .section table tr:nth-child(even) td {
            background-color: #f2f2f2;
        }

        .section table tr:hover td {
            background-color: #e1e1e1;
        }

        .section table td[colspan="4"] {
            text-align: center;
            font-style: italic;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp" class="active">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp">Account</a>
        </div>
    </div>

    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Dashboard</div>
        <div class="icons">
            <i class="fas fa-bell" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- View Sales Section -->
        <div class="section">
            <h2>View Sales (History)</h2>
            <%
			    String saleDateParam = request.getParameter("saleDate");
			
			    // Default to today's date if no date is provided
			    if (saleDateParam == null || saleDateParam.trim().isEmpty()) {
			        saleDateParam = java.time.LocalDate.now().toString();
			    }
			
			    // Convert the String saleDateParam to java.sql.Date
			    java.sql.Date saleDate = java.sql.Date.valueOf(saleDateParam);
			%>
            <table>
			    <thead>
			        <tr>
			            <th>Sale ID</th>
			            <th>Sale Date</th>
			            <th>Total Amount</th>
			            <th>Payment Method</th>
			        </tr>
			    </thead>
			    <tbody>
			        <%
			            SaleDAO saleDAO = new SaleDAO(); // Initialize DAO
			            List<Sale> salesList = saleDAO.getSalesByDate(saleDate); // Call the method
			
			            if (salesList != null && !salesList.isEmpty()) {
			                for (Sale sale : salesList) {
			        %>
			                    <tr>
			                        <td><%= sale.getSaleId() %></td>
			                        <td><%= sale.getSaleDate() %></td>
			                        <td><%= sale.getTotalAmount() %></td>
			                        <td><%= sale.getPaymentMethod() %></td>
			                    </tr>
			        <%
			                }
			            } else {
			        %>
			                <tr>
			                    <td colspan="4">No sales found for the selected date: <%= saleDate %>.</td>
			                </tr>
			        <%
			            }
			        %>
			    </tbody>
			</table>
        </div>

        <!-- View Product and Stock Section -->
        <div class="section">
            <h2>View Product and Stock</h2>
            <table>
                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Product Name</th>
                        <th>Quantity in Stock</th>
                        <th>Restock Level</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        // Fetch product list
                        ProductDAO productDAO = new ProductDAO();
                        List<Product> productList = productDAO.getAllActiveProducts();
                        if (productList != null && !productList.isEmpty()) {
                            for (Product product : productList) {
                    %>
                                <tr>
                                    <td><%= product.getProdId() %></td>
                                    <td><%= product.getProdName() %></td>
                                    <td><%= product.getQuantityStock() %></td>
                                    <td><%= product.getRestockLevel() %></td>
                                </tr>
                    <% 
                            }
                        } else { 
                    %>
                            <tr>
                                <td colspan="4">No products available.</td>
                            </tr>
                    <% 
                        } 
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
