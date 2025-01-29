package com.controller.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dao.ProductDAO;
import com.manager.DBConnection;
import com.model.Product;
import com.model.User;

public class UpdateProductStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters from request
        String prodIdString = request.getParameter("prodId");
        String newStatus = request.getParameter("status");

        try {
            // Parse prodId to int
            int prodId = Integer.parseInt(prodIdString);

            // Call DAO method to update the product status
            boolean isUpdated = updateProductStatus(prodId, newStatus);

            // Redirect or respond based on result
            if (isUpdated) {
                response.sendRedirect("ViewProductServlet?message=Product status updated successfully");
            } else {
                response.sendRedirect("ViewProductServlet?message=Error updating product status");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("ViewProductServlet?message=Invalid Product ID");
        }
    }

    private boolean updateProductStatus(int prodId, String newStatus) {
        boolean success = false;
        String query = "UPDATE products SET prodStatus = ? WHERE prodId = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newStatus); // Set status
            preparedStatement.setInt(2, prodId);      // Set product ID

            int rowsUpdated = preparedStatement.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}