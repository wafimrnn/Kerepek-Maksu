package com.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.dao.UserDAO;

public class UpdateAccountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user inputs
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        int userId = Integer.parseInt(request.getParameter("userId"));
        String userRole = (String) request.getSession().getAttribute("userRole");
        boolean isUpdated = false;

        // Check if the user is the owner and wants to update the account status
        if ("OWNER".equalsIgnoreCase(userRole)) {
            String status = request.getParameter("status"); // Add logic to update status if necessary
            // Optional: Use a DAO method to update status
        }

        // Update phone and address
        UserDAO userDAO = new UserDAO();
        isUpdated = userDAO.updateUserAccount(userId, phone, address);

        // Send JSON response with feedback
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (isUpdated) {
            out.println("{\"message\": \"Account updated successfully!\"}");
        } else {
            out.println("{\"error\": \"An error occurred while updating your account.\"}");
        }
    }
}
