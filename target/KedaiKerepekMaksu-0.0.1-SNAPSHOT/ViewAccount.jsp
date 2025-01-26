<%@ page import="java.util.List" %>
<%@ page import="com.model.User" %>
<%@ page import="com.dao.UserDAO" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Details</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="css/viewAccount.css">
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
            background-image: url('img/pisangImage.jpg');
            background-size: cover;
            background-position: center;
        }

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
            text-align: center;
            font-size: 20px;
            border-bottom: 1px solid #495057;
            padding-bottom: 10px;
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

        .head-bar {
            width: calc(100% - 220px);
            height: 60px;
            background-color: #F6C324;
            color: black;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
            position: fixed;
            top: 0;
            left: 220px;
            z-index: 1000;
        }
        
         .head-bar .title {
            font-size: 20px;
            font-weight: bold;
            color: black;
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
            text-align: left;
        }

        .button-container button {
            background-color: #007BFF;
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

    .staff-list {
        margin-top: 20px;
        padding: 15px;
        background-color: #f9f9f9;
        border: 1px solid black;
        border-radius: 10px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .staff-list h2 {
        font-size: 1.5rem;
        color: #333;
        margin-bottom: 10px;
        border-bottom: 2px solid black;
        padding-bottom: 5px;
    }

    .staff-account {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
        padding: 10px;
        background-color: #ffffff;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .staff-account p {
        margin: 0;
        font-size: 1rem;
        color: #555;
    }

    .staff-account button {
        background-color: #4CAF50;
        color: white;
        border: none;
        padding: 5px 10px;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s;
    }

    .staff-account button:hover {
        background-color: #45a049;
    }

    .staff-account span {
        font-weight: bold;
        color: #007BFF;
    }

    .staff-account span.inactive {
        color: #FF0000;
    }
</style>
</head>
<body data-role="<%= session.getAttribute("userRole") %>">
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
        <div class="title">Account Details</div>
        <div class="icons">
            <i class="fas fa-bell" id="notification-icon" title="Notifications"></i>
            <i class="fas fa-user-circle" title="Account"></i>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="blurred-box">
            <!-- Account Details Section -->
            <div class="header">
                <h1>My Account</h1>
                <div class="button-container">
                    <!-- Update Account Button -->
                    <a href="UpdateAccount.jsp" class="add-btn">Update Account</a>

                    <!-- Only show 'Create Staff Account' button for users with 'OWNER' role -->
                    <% if ("OWNER".equals(session.getAttribute("userRole"))) { %>
                        <button class="add-btn" onclick="location.href='CreateStaffAccount.jsp'">Create Staff Account</button>
                    <% } %>

                    <button class="add-btn logout-btn" onclick="logout()">Logout</button>
                </div>
            </div>

            <!-- Display Account Info -->
            <div class="account-info">
                <% 
                    // Retrieve the user data from session
                    User user = (User) session.getAttribute("user"); // Assuming the user is saved in session

                    if (user != null) {
                %>
                    <p><strong>Name:</strong> <%= user.getName() %></p>
                    <p><strong>Phone:</strong> <%= user.getPhone() %></p>
                    <p><strong>Address:</strong> <%= user.getAddress() %></p>
                    <p><strong>Role:</strong> <%= user.getRole() %></p>
                <% } else { %>
                    <p>User data not found. Please login again.</p>
                <% } %>
            </div>

            <!-- Display Staff Accounts (for OWNER only) -->
            <% if ("OWNER".equals(session.getAttribute("userRole"))) { %>
                <h2>Manage Staff Accounts</h2>
                <div class="staff-list">
                    <!-- Dynamically display staff accounts -->
                    <% 
					    User loggedInUser = (User) session.getAttribute("user");
					    if (loggedInUser != null) {
					        UserDAO userDAO = new UserDAO();
					        List<User> staffList = userDAO.getStaffByOwnerId(loggedInUser.getId());
					
					        for (User staff : staffList) { 
					            // Ensure getAccStatus is a valid method in your User class
					            String statusClass = "ACTIVE".equals(staff.getAccStatus()) ? "" : "inactive";
					%>
					            <div class="staff-account">
								    <p><strong>Staff Name:</strong> <%= staff.getName() %></p>
								    <p><strong>Status:</strong> <span id="status-<%= staff.getId() %>"><%= staff.getAccStatus() %></span></p>
								    <button onclick="toggleStaffStatus('<%= staff.getId() %>', '<%= staff.getAccStatus() %>')">
								        Update Status
								    </button>
								</div>
					<% 
					        }
					    } else { 
					%>
					        <p>User not logged in. Please log in again.</p>
					<% 
					    }
					%>
                </div>
            <% } %>
        </div>
    </div>

    <div id="notification-popup" style="display: none;">
        <ul id="notification-list"></ul>
    </div>
    <script src="js/notification.js"></script>

    <script>
        function toggleStaffStatus(staffId) {
            // Get the current status and toggle it for the specific staff (make AJAX call to update the status in the DB)
            var statusElement = document.getElementById(staffId);
            var currentStatus = statusElement.innerText;

            // Toggle the status between 'ACTIVE' and 'INACTIVE'
            if (currentStatus === 'ACTIVE') {
                statusElement.innerText = 'INACTIVE';
            } else {
                statusElement.innerText = 'ACTIVE';
            }
        }
        
        function logout() {
          // Redirect to login page
          window.location.href = "/LogoutServlet";
        }
    </script>
    <script src="js/account.js"></script>
</body>
</html>