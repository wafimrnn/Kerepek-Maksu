<%@ page import="com.model.Product, com.model.Food, com.model.Drink" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Product</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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

        /* Sidebar */
        .sidebar {
            width: 220px;
            background-color: #481D01;
            color: white;
            padding: 20px;
        }

        .sidebar h2 {
            text-align: center;
            border-bottom: 1px solid #495057;
            padding-bottom: 10px;
        }

        .sidebar a {
            display: block;
            color: white;
            text-decoration: none;
            padding: 10px;
            border-radius: 4px;
            transition: 0.3s;
        }

        .sidebar a.active {
            background-color: #F6C324; /* Background color for active link */
            color: black; /* Font color for active link */
        }
        
        .sidebar a:hover {
            background-color:  #FEECC3;
            color: black;
        }
        
        /* Head Bar Styling */
	    	.head-bar {
		        width: calc(100% - 220px); /* Full width minus the sidebar width */
		        height: 60px;
		        background-color:  #F6C324 ;
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
	    	}
		
		    .head-bar .title {
		        font-size: 20px;
		        font-weight: bold;
		    }
		
		    .head-bar .icons {
		        display: flex;
		        align-items: center;
		        gap: 15px;
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
            margin-top: 60px; /* Push content below the head bar */
        }
        
        .main-content h2 {
            font-size: 28px;
            color: white;
            margin-bottom: 20px;
        }
        

        .form-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input, select {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            padding: 10px 15px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .cancel-button {
            background-color: #dc3545;
        }
    </style>
    <script>
        function toggleCategoryFields() {
            const category = document.getElementById("prodStatus").value;
            document.getElementById("foodFields").style.display = category === "Food" ? "block" : "none";
            document.getElementById("drinkFields").style.display = category === "Drink" ? "block" : "none";
        }

        window.onload = toggleCategoryFields;
    </script>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Dashboard</h2>
        <a href="dashboard.html">Dashboard</a>
        <a href="ViewProductServlet" class="active">Product</a>
        <a href="CreateSales.jsp">Sales</a>
        <a href="Report.html">Report</a>
        <a href="ViewAccount.jsp">Account</a>
    </div>
    <!-- Head Bar -->
	<div class="head-bar">
	    <div class="title">Sales</div>
	    <div class="icons">
	        <i class="fas fa-bell" title="Notifications"></i>
	        <i class="fas fa-user-circle" title="Account"></i>
	    </div>
	</div>

    <!-- Main Content -->
    <div class="main-content">
        <h2>Update Product</h2>
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
</body>
</html>