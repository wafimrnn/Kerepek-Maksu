<%@ page import="java.util.List" %>
<%@ page import="com.model.Product" %>
<%@ page import="com.dao.ProductDAO" %>
<%@ page import="com.model.User" %> <!-- Import User model for role checking -->
<%@ page session="true" %> <!-- Allow session to access user info -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Products</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/viewProduct.css">
    <link rel="stylesheet" type="text/css" href="css/notification.css">
    <style>
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

        .main-content {
            flex: 1;
            padding: 20px;
            margin-top: 60px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }

        .blurred-box {
            background: #FBE39D;
            padding: 40px;
            border-radius: 10px;
            width: 80%;
            text-align: center;
        }

        .account-info p {
            font-size: 16px;
            margin-bottom: 10px;
        }

        .button-container button {
            background-color: #28a745;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin: 5px;
       	}
       	
       	 .main-content h1 {
            font-size: 28px;
            color: #343a40;
            margin-bottom: 20px;
        }
        
        /* Styles for Inactive Products Section */
        .inactive-products-section {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 5px;
            margin-top: 30px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        .inactive-products-section h2 {
            color: #333;
            font-size: 24px;
            margin-bottom: 20px;
        }

        .update-status-btn {
            background-color: #f44336; /* Red */
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .update-status-btn:hover {
            background-color: #d32f2f; /* Darker red on hover */
        }

        /* Modal Style */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            overflow: auto;
            padding-top: 60px;
        }

        .modal-content {
            background-color: #fff;
            margin: 5% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 40%;
            border-radius: 8px;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
        
        .add-btn {
            background: #28a745;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 5px;
            transition: 0.3s;
        }

        .add-btn:hover {
            background: #218838;
        }

        .product-catalog {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .product-card {
            background: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .product-card img {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 5px;
            margin-bottom: 10px;
        }

        .product-card h3 {
            font-size: 18px;
            margin-bottom: 10px;
        }

        .button-group {
            margin-top: 15px;
        }

        .update-btn, .delete-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }

        .update-btn:hover, .delete-btn:hover {
            background-color: #45a049;
        }

        .delete-btn {
            background-color: #f44336;
        }

        .delete-btn:hover {
            background-color: #d32f2f;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet" class="nav-link active">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp">Account</a>
        </div>
    </div>

    <!-- Head Bar -->
	<div class="head-bar">
	    <div class="title">Products</div>
	    <div class="icons">
	        <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
	        <i class="fas fa-user-circle" title="Account"></i>
	    </div>
	</div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="blurred-box">
            <!-- Header with Title and Add Product Button -->
            <div class="header">
                <h1>Products</h1>
                <a href="CreateProduct.html" class="add-btn">Add Product</a>
            </div>

            <!-- Product Catalog -->
			<div class="product-catalog">
			    <%
				    List<Product> products = (List<Product>) request.getAttribute("products");
				    if (products != null && !products.isEmpty()) {
				        for (Product product : products) {
				            String imagePath = product.getImagePath();
				            if (imagePath == null || imagePath.isEmpty()) {
				                imagePath = "img/default-image.jpg"; 
				            }

                            // If product is active, don't show "Update Status" button
                            if (!"inactive".equals(product.getProdStatus())) {
                %>
				            <div class="product-card">
				                <img src="<%= imagePath %>" alt="<%= product.getProdName() %>">
				                <h3><%= product.getProdName() %></h3>
				                <p>Price: RM <%= product.getProdPrice() %></p>
				                <p>Stock: <%= product.getQuantityStock() %></p>

				                <div class="button-group">
				                    <button class="update-btn" onclick="location.href='UpdateProductServlet?prodId=<%= product.getProdId() %>'">
				                        Update
				                    </button>
				                    <button class="delete-btn" onclick="confirmDelete('<%= product.getProdId() %>')">
				                        Delete
				                    </button>
				                </div>
				            </div>
                <% 
                            }
				        }
				    } else {
				%>
				        <p>No products available.</p>
				<%
				    }
				%>
			</div>

            <!-- Hardcoded Owner-Specific Section for Inactive Products -->
            <div class="inactive-products-section">
                
            </div>
        </div>
    </div>

    <!-- Simple Popup Modal for Confirmation -->
    <div id="popup-modal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closePopup()">&times;</span>
            <h2>Are you sure you want to update the product status?</h2>
            <div class="button-group">
                <button class="update-btn" onclick="updateStatus()">Yes</button>
                <button class="delete-btn" onclick="closePopup()">No</button>
            </div>
        </div>
    </div>

    <script>
 // Function to update the product status (mock-up)
    	function updateStatus() {
       	 	alert("Product status updated!");
       		closePopup();
   		}

        // Confirm delete action
        function confirmDelete(prodId) {
            if (confirm("Are you sure you want to delete this product? This action cannot be undone.")) {
                location.href = 'DeleteProductServlet?prodId=' + prodId;
            }
        }
    </script>

    <!-- Optional: Notification Popup -->
    <div id="notification-popup" style="display: none;">
        <ul id="notification-list"></ul>
    </div>
    <script src="js/notification.js"></script>
</body>
</html>
