<%@ page import="com.model.Product, com.model.Food, com.model.Drink" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Product</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/notification.css">
    <script>
        function toggleCategoryFields() {
            const category = document.getElementById("prodStatus").value;
            document.getElementById("foodFields").style.display = category === "Food" ? "block" : "none";
            document.getElementById("drinkFields").style.display = category === "Drink" ? "block" : "none";
        }

        window.onload = toggleCategoryFields;
    </script>
    <style type="text/css">
    @charset "UTF-8";

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
            background-color: rgba(241, 241, 109, 0.62); /* Semi-transparent background color */
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
            background-color: #495057;
        }

        .nav-links a.active {
            background-color: #f1ea82; /* Background color for active link */
            color: #5c5c52; /* Font color for active link */
        }
        
        /* Head Bar Styling */
        .head-bar {
            width: calc(100% - 220px); /* Full width minus the sidebar width */
            height: 60px;
            background-color: rgba(227, 227, 80, 0.62); /* Semi-transparent background color */
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
            color: #ffffff;
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
            position: relative; /* Position relative for the overlay */
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            margin-top: 60px; /* Push content below the head bar */
            overflow: hidden; /* Prevent overflow */
        }
        
        .main-content::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-image: url('img/pisangImage.jpg'); /* Path to your image */
            background-size: cover; /* Cover the entire area */
            background-position: center; /* Center the image */
            background-repeat: no-repeat; /* Prevent the image from repeating */
            filter: blur(1px); /* Adjust blur intensity */
            z-index: -1; /* Push background below all content */
        }
        
        .main-content h1{
        text-align: center;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 10px;
        }

        .header h1 {
            font-size: 28px;
            color: #343a40;
        }
        
        .blurred-box {
            position: relative;
            z-index: 1;
            padding: 40px;
            background: rgba(255, 250, 171, 0.62);
            backdrop-filter: blur(8px); /* Ensure this is applied correctly */
            border-radius: 10px;
            margin-top: 20px; /* Set a height to center vertically */
            width: 80%;
            text-align: center;
            
        }

        /* Form Styling */
        .form-container {
            background-color: #f9f9f9;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            max-width: 500px;
            margin: 0 auto;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            color: #555;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 8px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .form-group input[type="file"] {
            border: none;
        }

        .form-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .form-buttons button {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            transition: background 0.3s ease;
        }

        .form-buttons .submit-btn {
            background-color: #007BFF;
            color: white;
        }

        .form-buttons .submit-btn:hover {
            background-color: #0056b3;
        }

        .form-buttons .cancel-btn {
            background-color: #dc3545;
            color: white;
        }

        .form-buttons .cancel-btn:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet" class="active">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp">Account</a>
        </div>
    </div>
    <!-- Head Bar -->
	<div class="head-bar">
	    <div class="title">Sales</div>
	    <div class="icons">
	        <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
	        <i class="fas fa-user-circle" title="Account"></i>
	    </div>
	</div>

    <!-- Main Content -->
    <div class="main-content">
        <h2 style="color: white;">Update Product</h2>
        <div class="form-container">
            <%
                Product product = (Product) request.getAttribute("product");
                if (product == null) {
            %>
                <p style="color:red;">Product not found.</p>
            <% } else { %>
            <form action="UpdateProductServlet" method="post" enctype="multipart/form-data">
		    <input type="hidden" name="prodId" value="<%= product.getProdId() %>">
		
		    <div class="form-group">
		        <label for="prodName">Product Name:</label>
		        <input type="text" id="prodName" name="prodName" value="<%= product.getProdName() %>" required>
		    </div>
		
		    <div class="form-group">
		        <label for="prodPrice">Price:</label>
		        <input type="number" id="prodPrice" name="prodPrice" step="0.01" value="<%= product.getProdPrice() %>" required>
		    </div>
		
		    <div class="form-group">
		        <label for="quantityStock">Stock:</label>
		        <input type="number" id="quantityStock" name="quantityStock" value="<%= product.getQuantityStock() %>" required>
		    </div>
		
		    <!-- Category Field -->
		    <div class="form-group">
		        <label for="category">Category:</label>
		        <select id="category" name="category" onchange="toggleCategoryFields()" required>
		            <option value="Food" <%= product instanceof Food ? "selected" : "" %>>Food</option>
		            <option value="Drink" <%= product instanceof Drink ? "selected" : "" %>>Drink</option>
		        </select>
		    </div>
		
		    <!-- Dynamic Fields for Food -->
		    <div id="foodFields" style="<%= product instanceof Food ? "display:block;" : "display:none;" %>">
		        <div class="form-group">
		            <label for="packagingType">Packaging Type:</label>
		            <input type="text" id="packagingType" name="packagingType" value="<%= product instanceof Food ? ((Food) product).getPackagingType() : "" %>">
		        </div>
		        <div class="form-group">
		            <label for="weight">Weight (kg):</label>
		            <input type="number" id="weight" name="weight" step="0.01" value="<%= product instanceof Food ? ((Food) product).getWeight() : "" %>">
		        </div>
		    </div>
		
		    <!-- Dynamic Fields for Drink -->
		    <div id="drinkFields" style="<%= product instanceof Drink ? "display:block;" : "display:none;" %>">
		        <div class="form-group">
		            <label for="volume">Volume (L):</label>
		            <input type="number" id="volume" name="volume" step="0.01" value="<%= product instanceof Drink ? ((Drink) product).getVolume() : "" %>">
		        </div>
		    </div>
		
		    <!-- Product Status Field -->
		    <div class="form-group">
		        <label for="prodStatus">Status:</label>
		        <select id="prodStatus" name="prodStatus" required>
		            <option value="Active" <%= "Active".equals(product.getProdStatus()) ? "selected" : "" %>>Active</option>
		            <option value="Inactive" <%= "Inactive".equals(product.getProdStatus()) ? "selected" : "" %>>Inactive</option>
		        </select>
		    </div>
		
		    <div class="form-group">
		        <label for="image">Product Image:</label>
		        <input type="file" id="image" name="image">
		        <small>Upload a new image or leave blank to keep the current image.</small>
		    </div>
		
		    <button type="submit">Update Product</button>
		    <button type="button" class="cancel-button" onclick="window.location.href='ViewProductServlet'">Cancel</button>
		</form>
		
		<script>
		    function toggleCategoryFields() {
		        const category = document.getElementById("category").value;
		        document.getElementById("foodFields").style.display = category === "Food" ? "block" : "none";
		        document.getElementById("drinkFields").style.display = category === "Drink" ? "block" : "none";
		    }
		
		    window.onload = toggleCategoryFields;
		</script>
            <% } %>
        </div>
    </div>
    <div id="notification-popup" style="display: none;">
	    <ul id="notification-list"></ul>
	</div>
	<script src="js/notification.js"></script>
</body>
</html>