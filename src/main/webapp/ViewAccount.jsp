<%@ page import="com.dao.UserDAO" %>
<%@ page import="com.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Details</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/viewAccount.css">
    <link rel="stylesheet" type="text/css" href="css/notification.css">
</head>
<body data-role="<%= session.getAttribute("userRole") %>">
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

    <div class="head-bar">
        <div class="title">Account Details</div>
        <div class="icons">
            <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <div class="main-content">
        <div class="blurred-box">
            <div class="header">
                <h1>My Account</h1>
                <div class="button-container">
                    <a href="UpdateAccount.jsp" class="add-btn">Update Account</a>

                    <% 
                        String userRole = (String) session.getAttribute("userRole");
                        if ("OWNER".equals(userRole)) {
                            // Show "Create Staff Account" button for OWNER role
                    %>
                        <button class="add-btn" onclick="location.href='CreateStaffAccount.jsp'">Create Staff Account</button>
                    <% } %>

                    <button class="add-btn logout-btn" onclick="location.href='/Kedai-Kerepek-Maksu-Inventory-System/LogoutServlet'">Logout</button>
                </div>
            </div>

            <!-- Fetch and Display User Info -->
            <div class="account-info">
                <%
                    UserDAO userDAO = new UserDAO();
                    User currentUser = userDAO.getUserById((Integer) session.getAttribute("userId")); // Assuming userId is stored in session

                    if (currentUser != null) {
                %>
                    <p><strong>Name:</strong> <%= currentUser.getName() %></p>
                    <p><strong>Phone:</strong> <%= currentUser.getPhone() %></p>
                    <p><strong>Address:</strong> <%= currentUser.getAddress() %></p>
                    <p><strong>Role:</strong> <%= currentUser.getRole() %></p>
                    <p><strong>Status:</strong> <%= "ACTIVE".equals(currentUser.getRole()) ? "ACTIVE" : "INACTIVE" %></p>
                <%
                    }
                %>
            </div>

            <!-- Display Staff Accounts if OWNER role -->
            <% if ("OWNER".equals(userRole)) { %>
                <h2>Manage Staff Accounts</h2>
                <div class="staff-list">
                    <% 
                        // Fetch staff accounts of the current owner
                        List<User> staffList = userDAO.getStaffByOwnerId(currentUser.getId());
                        for (User staff : staffList) {
                    %>
                        <div class="staff-account">
                            <p><strong>Staff Name:</strong> <%= staff.getName() %></p>
                            <p><strong>Status:</strong> <span id="staffStatus<%= staff.getId() %>"><%= "ACTIVE".equals(staff.getRole()) ? "ACTIVE" : "INACTIVE" %></span></p>
                            <button onclick="toggleStaffStatus('<%= staff.getId() %>')">Toggle Status</button>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>
    </div>

    <div id="notification-popup" style="display: none;">
        <ul id="notification-list"></ul>
    </div>

    <script src="js/account.js"></script>
    <script src="js/notification.js"></script>

    <script>
        function toggleStaffStatus(staffId) {
            var statusElement = document.getElementById('staffStatus' + staffId);
            var currentStatus = statusElement.innerText;

            if (currentStatus === 'ACTIVE') {
                statusElement.innerText = 'INACTIVE';
            } else {
                statusElement.innerText = 'ACTIVE';
            }
        }
    </script>
</body>
</html>
