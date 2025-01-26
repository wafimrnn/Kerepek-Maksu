package com.controller.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.dao.ProductDAO;
import com.model.Product;
import com.model.User;

public class UpdateProductStatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is logged in and is an owner
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"OWNER".equals(currentUser.getRole())) {
            response.sendRedirect("Login.jsp"); // Redirect to login if not an owner
            return;
        }

        // Get product ID from request
        String prodIdStr = request.getParameter("prodId");
        if (prodIdStr == null || prodIdStr.isEmpty()) {
            response.sendRedirect("ViewProductServlet"); // Redirect to product list if no product ID is passed
            return;
        }

        int prodId = Integer.parseInt(prodIdStr);
        
        // Retrieve product from the database
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getProductById(prodId);

        if (product != null) {
            // Update the product status to "active" (or you can toggle between 'active' and 'inactive')
            product.setProdStatus("active");  // Example: change status to active
            productDAO.updateProductStatus(product);

            // Redirect back to the product list page after the update
            response.sendRedirect("ViewProductServlet");
        } else {
            response.sendRedirect("ViewProductServlet"); // If product not found, redirect to product list
        }
    }
}