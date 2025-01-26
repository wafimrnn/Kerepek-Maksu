package com.controller.account;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import com.dao.UserDAO;
import com.model.User;

public class UpdateAccountServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Ensure only OWNER can update statuses
        if (currentUser == null || !"OWNER".equals(currentUser.getRole())) {
            response.sendRedirect("Login.html");
            return;
        }

        String staffId = request.getParameter("staffId");
        String newStatus = request.getParameter("newStatus");

        // Add logging or error checks
        if (staffId == null || newStatus == null) {
            response.getWriter().write("FAIL: Missing parameters");
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean isUpdated = userDAO.updateAccountStatus(Integer.parseInt(staffId), newStatus);

        // Send success or failure response
        response.getWriter().write(isUpdated ? "SUCCESS" : "FAIL");
    }
}