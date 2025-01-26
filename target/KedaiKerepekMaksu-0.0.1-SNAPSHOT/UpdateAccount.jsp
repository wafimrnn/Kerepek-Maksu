<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Account</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/updateAccount.css">
    <link rel="stylesheet" type="text/css" href="css/notification.css">
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Kedai Kerepek Maksu</h2>
        <div class="nav-links">
            <a href="DashboardHome.jsp">Dashboard</a>
            <a href="ViewProductServlet">Product</a>
            <a href="CreateSales.jsp">Sales</a>
            <a href="Report.html">Report</a>
            <a href="ViewAccount.jsp" class="nav-link active">Account</a>
        </div>
    </div>

    <!-- Head Bar -->
    <div class="head-bar">
        <div class="title">Update Account</div>
        <div class="icons">
            <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
    	<div class="blurred-box">
        <h1>Update Account</h1>
    
        <div class="form-container">
            <form action="UpdateAccountServlet" method="post">
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" value="<%= request.getAttribute("userName") %>" readonly>
                </div><br>
                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="text" id="phone" name="phone" value="<%= request.getAttribute("userPhone") %>" required>
                </div><br>
                <div class="form-group">
                    <label for="address">Address:</label>
                    <textarea id="address" name="address" required><%= request.getAttribute("userAddress") %></textarea>
                </div><br>
                <div class="form-group">
                    <label for="role">Role:</label>
                    <input type="text" id="role" name="role" value="<%= request.getAttribute("userRole") %>" readonly>
                </div><br>
                <div class="form-group">
                    <label for="status">Account Status:</label>
                    <input type="text" id="status" name="status" value="<%= request.getAttribute("accStatus") %>" readonly>
                </div><br>
                <input type="hidden" name="userId" value="<%= session.getAttribute("userId") %>">
                <div class="form-actions">
                    <button type="submit">Update</button>
                    <button type="button" onclick="window.location.href='ViewAccount.jsp'">Cancel</button>
                </div>
            </form>
        	</div>
    	</div>
    </div>
    <div id="notification-popup" style="display: none;">
	    <ul id="notification-list"></ul>
	</div>
	<script src="js/notification.js"></script>
</body>
</html>