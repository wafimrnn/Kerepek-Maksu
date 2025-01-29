package com.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import com.dao.UserDAO;
import com.model.User;

public class UpdateAccountServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type to handle UTF-8 properly
        request.setCharacterEncoding("UTF-8");

        try {
            // Extract form parameters
            int userId = Integer.parseInt(request.getParameter("userId").trim()); // Hidden field in form
            String username = request.getParameter("username").trim();
            String phone = request.getParameter("phone").trim();
            String address = request.getParameter("address").trim();
            String role = request.getParameter("role").toUpperCase();

         // Validate role
         if (!"OWNER".equals(role) && !"STAFF".equals(role)) {
             request.setAttribute("error", "Invalid role value.");
             request.getRequestDispatcher("UpdateAccount.jsp").forward(request, response);
             return;
         }
            String status = request.getParameter("status").trim();

            // Validate input fields
            if (username.isEmpty() || phone.isEmpty() || address.isEmpty() || role.isEmpty() || status.isEmpty()) {
                request.setAttribute("error", "All fields are required.");
                preserveFormInputs(request, username, phone, address, role, status);
                request.getRequestDispatcher("UpdateAccount.jsp").forward(request, response);
                return;
            }

            // Check if user exists
            User existingUser = userDAO.getUserById(userId);
            if (existingUser == null) {
                request.setAttribute("error", "User not found.");
                preserveFormInputs(request, username, phone, address, role, status);
                request.getRequestDispatcher("UpdateAccount.jsp").forward(request, response);
                return;
            }

            // Attempt to update the user's account
            boolean isUpdated = userDAO.updateUserAccount(userId, username, phone, address, role, status);
            if (isUpdated) {
                // Redirect to the ViewAccountServlet to refresh the data
                response.sendRedirect("ViewAccountServlet");
            } else {
                request.setAttribute("error", "Failed to update account. Please try again.");
                preserveFormInputs(request, username, phone, address, role, status);
                request.getRequestDispatcher("UpdateAccount.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid User ID.");
            request.getRequestDispatcher("UpdateAccount.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("UpdateAccount.jsp").forward(request, response);
        }
    }

    /**
     * Preserves form inputs in the request scope to repopulate the form on errors.
     */
    private void preserveFormInputs(HttpServletRequest request, String username, String phone, String address, String role, String status) {
        request.setAttribute("username", username);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
        request.setAttribute("role", role);
        request.setAttribute("status", status);
    }
}