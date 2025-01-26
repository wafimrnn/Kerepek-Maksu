package com.controller.product;

import com.dao.DrinkDAO;
import com.dao.FoodDAO;
import com.dao.ProductDAO;
import com.manager.BlobStorage;
import com.manager.DBConnection;
import com.model.Drink;
import com.model.Food;
import com.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;

@MultipartConfig
public class CreateProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final FoodDAO foodDAO = new FoodDAO();
    private final DrinkDAO drinkDAO = new DrinkDAO();
    private final BlobStorage blobService = new BlobStorage();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve form parameters
            String prodName = request.getParameter("prodName");
            double prodPrice = Double.parseDouble(request.getParameter("prodPrice"));
            int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));
            int restockLevel = Integer.parseInt(request.getParameter("restockLevel"));
            String expiryDateStr = request.getParameter("expiryDate");
            String category = request.getParameter("category");
            Part filePart = request.getPart("image");

            if (filePart == null || prodName == null || category == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing form data.");
                return;
            }

            // Convert expiryDate string to java.sql.Date if not null or empty
            java.sql.Date expiryDate = null;
            if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
                expiryDate = java.sql.Date.valueOf(expiryDateStr); // Converts String (YYYY-MM-DD) to java.sql.Date
            }

            // Upload the image to blob storage
            String imageUrl = blobService.uploadImage(filePart.getInputStream(), filePart.getSize(), filePart.getSubmittedFileName());

            // Create Product object
            Product product = new Product();
            product.setProdName(prodName);
            product.setProdPrice(prodPrice);
            product.setQuantityStock(quantityStock);
            product.setRestockLevel(restockLevel);
            product.setExpiryDate(expiryDate);  // Pass java.sql.Date object
            product.setImagePath(imageUrl);

            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false);

                // Insert the product into the product table and get the generated ID
                int prodId = productDAO.insertProduct(product);

                // Handle Food or Drink based on the category
                if ("Food".equalsIgnoreCase(category)) {
                    String packagingType = request.getParameter("packagingType");
                    double weight = Double.parseDouble(request.getParameter("weight"));
                    Food food = new Food();
                    food.setProdId(prodId);  // Set product ID
                    food.setPackagingType(packagingType);
                    food.setWeight(weight);

                    foodDAO.insertFood(conn, food);  // Pass Food object to the DAO
                } else if ("Drink".equalsIgnoreCase(category)) {
                    double volume = Double.parseDouble(request.getParameter("volume"));
                    Drink drink = new Drink();
                    drink.setProdId(prodId);  // Set product ID
                    drink.setVolume(volume);

                    drinkDAO.insertDrink(conn, drink);  // Pass Drink object to the DAO
                }

                conn.commit();

                // Provide feedback and redirect
                request.setAttribute("successMessage", "Product successfully added!");
                response.sendRedirect("ViewProduct.jsp");
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
