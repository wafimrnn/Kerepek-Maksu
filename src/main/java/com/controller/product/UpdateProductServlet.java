package com.controller.product;

import com.manager.DBConnection;
import com.dao.ProductDAO;
import com.manager.BlobStorage;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@MultipartConfig
public class UpdateProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String prodIdParam = request.getParameter("prodId");
        if (prodIdParam == null || prodIdParam.isEmpty()) {
            response.sendRedirect("ViewProductServlet");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            int prodId = Integer.parseInt(prodIdParam);
            String query = "SELECT * FROM Products WHERE PROD_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, prodId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Product product = new Product();
                        product.setProdId(prodId);
                        product.setProdName(rs.getString("PROD_NAME"));
                        product.setProdPrice(rs.getDouble("PROD_PRICE"));
                        product.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                        product.setProdStatus(rs.getString("PROD_STATUS"));
                        product.setImagePath(rs.getString("IMAGE_PATH")); // Existing image URL

                        request.setAttribute("product", product);
                        request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                        return;
                    }
                }
            }
            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProductServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int prodId = Integer.parseInt(request.getParameter("prodId"));
            String prodName = request.getParameter("prodName");
            double prodPrice = Double.parseDouble(request.getParameter("prodPrice"));
            int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));
            String category = request.getParameter("prodStatus"); // Food or Drink

            // Handle image upload
            Part imagePart = request.getPart("image");
            String imageUrl = request.getParameter("existingImageUrl");
            if (imagePart != null && imagePart.getSize() > 0) {
                BlobStorage blobManager = new BlobStorage();
                imageUrl = blobManager.uploadImage(imagePart, "product-images");
            }

            // Determine category and update
            Product product;
            if ("Food".equalsIgnoreCase(category)) {
                product = new Food();
                ((Food) product).setPackagingType(request.getParameter("packagingType"));
                ((Food) product).setWeight(Double.parseDouble(request.getParameter("weight")));
            } else if ("Drink".equalsIgnoreCase(category)) {
                product = new Drink();
                ((Drink) product).setVolume(Double.parseDouble(request.getParameter("volume")));
            } else {
                product = new Product(); // Default to base class if no specific type
            }

            // Set common fields
            product.setProdId(prodId);
            product.setProdName(prodName);
            product.setProdPrice(prodPrice);
            product.setQuantityStock(quantityStock);
            product.setImagePath(imageUrl);

            // Update using DAO
            ProductDAO.updateProduct(product);

            response.sendRedirect("ViewProductServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewProductServlet");
        }
    }
}