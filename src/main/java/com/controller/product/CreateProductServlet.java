package com.controller.product;

import com.dao.DrinkDAO;
import com.dao.FoodDAO;
import com.dao.ProductDAO;
import com.manager.BlobStorage;
import com.manager.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@MultipartConfig
public class CreateProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final FoodDAO foodDAO = new FoodDAO();
    private final DrinkDAO drinkDAO = new DrinkDAO();
    private final BlobStorage blobService = new BlobStorage();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String prodName = request.getParameter("prodName");
            double prodPrice = Double.parseDouble(request.getParameter("prodPrice"));
            int quantityStock = Integer.parseInt(request.getParameter("quantityStock"));
            int restockLevel = Integer.parseInt(request.getParameter("restockLevel"));
            String expiryDate = request.getParameter("expiryDate");
            String category = request.getParameter("category");
            Part filePart = request.getPart("image");

            if (filePart == null || prodName == null || category == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing form data.");
                return;
            }

            String imageUrl = blobService.uploadImage(filePart.getInputStream(), filePart.getSize(), filePart.getSubmittedFileName());

            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false);

                int prodId = productDAO.insertProduct(prodName, prodPrice, quantityStock, restockLevel, expiryDate, imageUrl);

                if ("Food".equalsIgnoreCase(category)) {
                    String packagingType = request.getParameter("packagingType");
                    double weight = Double.parseDouble(request.getParameter("weight"));
                    foodDAO.insertFood(conn, prodId, packagingType, weight);
                } else if ("Drink".equalsIgnoreCase(category)) {
                    double volume = Double.parseDouble(request.getParameter("volume"));
                    drinkDAO.insertDrink(conn, prodId, volume);
                }

                conn.commit();
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
