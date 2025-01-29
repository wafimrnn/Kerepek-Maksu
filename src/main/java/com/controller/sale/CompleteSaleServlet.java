package com.controller.sale;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dao.SaleDAO;
import com.manager.DBConnection;
import com.model.Sale;
import com.model.SaleItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/completeSale")  // URL pattern where the servlet will be accessible
public class CompleteSaleServlet extends HttpServlet {

    private SaleDAO saleDAO;

    @Override
    public void init() {
        // Initialize the SaleDAO
        saleDAO = new SaleDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Parse JSON data
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject requestData = new JSONObject(sb.toString());

            // Extract details
            double totalAmount = requestData.getDouble("totalAmount");
            String paymentMethod = requestData.getString("paymentMethod");
            String saleDate = requestData.getString("saleDate");

            // Get userId from session
            HttpSession session = request.getSession();
            int userId = (Integer) session.getAttribute("userId");

            // Create Sale object
            Sale sale = new Sale();
            sale.setSaleDate(saleDate);
            sale.setTotalAmount(totalAmount);
            sale.setPaymentMethod(paymentMethod);
            sale.setUserId(userId);

            // Extract and validate order items
            JSONArray orderItems = requestData.getJSONArray("orderItems");
            List<SaleItem> saleItems = new ArrayList<>();
            for (int i = 0; i < orderItems.length(); i++) {
                JSONObject item = orderItems.getJSONObject(i);
                SaleItem saleItem = new SaleItem();
                saleItem.setProdId(item.getInt("prodId"));
                saleItem.setQuantity(item.getInt("qty"));
                saleItem.setSubTotal(item.getDouble("subtotal"));
                saleItems.add(saleItem);
            }
            if (saleItems.isEmpty()) {
                throw new IllegalArgumentException("Sale items cannot be empty.");
            }
            sale.setSaleItems(saleItems); // Properly set saleItems in Sale

            // Insert sale and sale items
            int saleId = saleDAO.insertSale(sale); // Inserts sale
            try (Connection conn = DBConnection.getConnection()) {
                saleDAO.insertSaleItems(conn, saleId, saleItems); // Inserts sale items
            }

            // Send success response
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", "success");
            responseJson.put("message", "Sale completed successfully!");
            response.getWriter().write(responseJson.toString());
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            response.getWriter().write(errorResponse.toString());
        }
    }

}
