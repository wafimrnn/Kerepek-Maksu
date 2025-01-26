<%@ page import="java.util.List" %>
<%@ page import="com.model.User" %>
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

                    <button class="add-btn logout-btn" onclick="location.href='/LogoutServlet'">Logout</button>
                </div>
            </div>

            <!-- Display Account Info (Hardcoded for now) -->
            <div class="account-info">
                <p><strong>Name:</strong> MaksuZah</p>
                <p><strong>Phone:</strong> 01115058369</p>
                <p><strong>Address:</strong> Merlimau, Melaka</p>
                <p><strong>Role:</strong> OWNER</p>
                <p><strong>Status:</strong> ACTIVE</p>
            </div>

            <!-- Display Staff Accounts (for OWNER only) -->
            <% if ("OWNER".equals(session.getAttribute("userRole"))) { %>
                <h2>Manage Staff Accounts</h2>
                <div class="staff-list">
                    <!-- Hardcoded staff accounts list -->
                    <div class="staff-account">
                        <p><strong>Staff Name:</strong> NurSiti</p>
                        <p><strong>Status:</strong> <span id="staffStatus1">ACTIVE</span></p>
                        <button onclick="toggleStaffStatus('staffStatus1')">Toggle Status</button>
                    </div>
                    <div class="staff-account">
                        <p><strong>Staff Name:</strong> FatinNjh</p>
                        <p><strong>Status:</strong> <span id="staffStatus2">INACTIVE</span></p>
                        <button onclick="toggleStaffStatus('staffStatus2')">Toggle Status</button>
                    </div>
                    <!-- Add more staff members as needed -->
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
        function toggleStaffStatus(staffStatusId) {
            // Get the current status and toggle it for the specific staff
            var statusElement = document.getElementById(staffStatusId);
            var currentStatus = statusElement.innerText;

            // Toggle the status between 'ACTIVE' and 'INACTIVE'
            if (currentStatus === 'ACTIVE') {
                statusElement.innerText = 'INACTIVE';
            } else {
                statusElement.innerText = 'ACTIVE';
            }
        }
    </script>
</body>
</html>