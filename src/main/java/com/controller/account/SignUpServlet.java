package com.controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.UserDAO;

public class SignUpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        UserDAO userDAO = new UserDAO();

        try {
            // Check if username already exists
            if (userDAO.isUsernameTaken(username)) {
                response.sendRedirect("Login.html?message=Username is already taken. Please choose another.");
                return;
            }

            // Attempt to create the account
            boolean accountCreated = userDAO.createOwner(username, password, phone, address);

            if (accountCreated) {
                // Account created successfully
                response.sendRedirect("Login.html?message=Account created successfully! Please log in.");
            } else {
                // Account creation failed
                response.sendRedirect("Login.html?message=Error creating account. Please try again later.");
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            response.sendRedirect("Login.html?message=An unexpected error occurred. Please try again.");
        }
    }
}