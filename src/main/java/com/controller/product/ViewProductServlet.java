package com.controller.product;

import com.dao.ProductDAO;
import com.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ViewProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Starting ViewProductServlet...");

        // Fetch products from DAO
        List<Product> products = productDAO.getAllActiveProducts();

        // Check if JSON response is required
        String responseType = request.getParameter("responseType");
        if ("json".equalsIgnoreCase(responseType)) {
            // Set JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convert products to JSON
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(products);

            // Write JSON to response
            response.getWriter().write(json);
            return;
        }

        // Set products in request scope
        request.setAttribute("products", products);

        // Disable caching to prevent stale data issues
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Forward to JSP
        request.getRequestDispatcher("ViewProduct.jsp").forward(request, response);
    }
}
