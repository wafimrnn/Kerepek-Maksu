package com.dao;

import com.model.Drink;
import com.model.Food;
import com.model.Product;
import com.manager.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Get all active products
    public List<Product> getAllActiveProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.PROD_ID, p.PROD_NAME, p.PROD_PRICE, p.QUANTITY_STOCK, p.IMAGE_PATH, " +
                     "       f.PACKAGING_TYPE, f.WEIGHT, d.VOLUME " +
                     "FROM Products p " +
                     "LEFT JOIN Food f ON p.PROD_ID = f.PROD_ID " +
                     "LEFT JOIN Drink d ON p.PROD_ID = d.PROD_ID " +
                     "WHERE p.PROD_STATUS = 'ACTIVE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String imagePath = rs.getString("IMAGE_PATH");
                if (imagePath == null || imagePath.isEmpty()) {
                    imagePath = "img/default-image.jpg"; // Default image if none provided
                }

                if (rs.getString("PACKAGING_TYPE") != null) {
                    Food food = new Food();
                    food.setProdId(rs.getInt("PROD_ID"));
                    food.setProdName(rs.getString("PROD_NAME"));
                    food.setProdPrice(rs.getDouble("PROD_PRICE"));
                    food.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                    food.setImagePath(imagePath);  // Fallback image
                    food.setPackagingType(rs.getString("PACKAGING_TYPE"));
                    food.setWeight(rs.getDouble("WEIGHT"));
                    products.add(food);
                } else if (rs.getDouble("VOLUME") > 0) {
                    Drink drink = new Drink();
                    drink.setProdId(rs.getInt("PROD_ID"));
                    drink.setProdName(rs.getString("PROD_NAME"));
                    drink.setProdPrice(rs.getDouble("PROD_PRICE"));
                    drink.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                    drink.setImagePath(imagePath);  // Fallback image
                    drink.setVolume(rs.getDouble("VOLUME"));
                    products.add(drink);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // Insert product (using Oracle sequence for PROD_ID)
    public int insertProduct(Product product) throws SQLException {
        // Step 1: Retrieve the next PROD_ID from the sequence
        String sequenceQuery = "SELECT PRODUCTS_SEQ.NEXTVAL FROM dual";
        int prodId = -1;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement seqPs = conn.prepareStatement(sequenceQuery);
             ResultSet rs = seqPs.executeQuery()) {
            
            if (rs.next()) {
                prodId = rs.getInt(1);  // Get the next value from the sequence
            }
        }

        if (prodId == -1) {
            throw new SQLException("Failed to retrieve PROD_ID from sequence.");
        }

        // Step 2: Insert the product into the Products table using the retrieved PROD_ID
        String sql = "INSERT INTO Products (PROD_ID, PROD_NAME, PROD_PRICE, QUANTITY_STOCK, RESTOCK_LEVEL, EXPIRY_DATE, IMAGE_PATH, PROD_STATUS) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prodId);  // Use the retrieved PROD_ID
            ps.setString(2, product.getProdName());
            ps.setDouble(3, product.getProdPrice());
            ps.setInt(4, product.getQuantityStock());
            ps.setInt(5, product.getRestockLevel());

            if (product.getExpiryDate() != null) {
                ps.setDate(6, new java.sql.Date(product.getExpiryDate().getTime()));  // Handle expiry date
            } else {
                ps.setNull(6, java.sql.Types.DATE);  // If no expiry date is provided, set as NULL
            }

            ps.setString(7, product.getImagePath());
            ps.setString(8, "ACTIVE");

            // Execute the insert statement
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to insert product into Products table.", e);
        }

        // Step 3: Insert into the child tables (Foods or Drinks)
        if (product instanceof Food) {
            Food food = (Food) product;
            String foodSql = "INSERT INTO Foods (PROD_ID, PACKAGING_TYPE, WEIGHT) VALUES (?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement psFood = conn.prepareStatement(foodSql)) {
                psFood.setInt(1, prodId);  // Link the food with the generated PROD_ID
                psFood.setString(2, food.getPackagingType());
                psFood.setDouble(3, food.getWeight());
                psFood.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Failed to insert product into Foods table.", e);
            }
        } else if (product instanceof Drink) {
            Drink drink = (Drink) product;
            String drinkSql = "INSERT INTO Drinks (PROD_ID, VOLUME) VALUES (?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement psDrink = conn.prepareStatement(drinkSql)) {
                psDrink.setInt(1, prodId);  // Link the drink with the generated PROD_ID
                psDrink.setDouble(2, drink.getVolume());
                psDrink.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Failed to insert product into Drinks table.", e);
            }
        }

        return prodId;  // Return the generated PROD_ID
    }


    // Update product details
    public static void updateProduct(Product product) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            String updateSQL = "UPDATE Products SET PROD_NAME = ?, PROD_PRICE = ?, QUANTITY_STOCK = ?, IMAGE_PATH = ? WHERE PROD_ID = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                ps.setString(1, product.getProdName());
                ps.setDouble(2, product.getProdPrice());
                ps.setInt(3, product.getQuantityStock());
                ps.setString(4, product.getImagePath());
                ps.setInt(5, product.getProdId());
                ps.executeUpdate();
            }

            // Update specific fields for Food or Drink
            if (product instanceof Food) {
                Food food = (Food) product;
                String updateFoodSQL = "UPDATE Foods SET PACKAGING_TYPE = ?, WEIGHT = ? WHERE PROD_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateFoodSQL)) {
                    ps.setString(1, food.getPackagingType());
                    ps.setDouble(2, food.getWeight());
                    ps.setInt(3, food.getProdId());
                    ps.executeUpdate();
                }
            } else if (product instanceof Drink) {
                Drink drink = (Drink) product;
                String updateDrinkSQL = "UPDATE Drinks SET VOLUME = ? WHERE PROD_ID = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateDrinkSQL)) {
                    ps.setDouble(1, drink.getVolume());
                    ps.setInt(2, drink.getProdId());
                    ps.executeUpdate();
                }
            }
        }
    }

    private static final String GET_PRODUCT_BY_ID_QUERY = "SELECT * FROM PRODUCTS WHERE PROD_ID = ?";
    private static final String UPDATE_PRODUCT_STATUS_QUERY = "UPDATE PRODUCTS SET PROD_STATUS = ? WHERE PROD_ID = ?";

    // Get product by ID
    public Product getProductById(int prodId) {
        Product product = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PRODUCT_BY_ID_QUERY)) {
            stmt.setInt(1, prodId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setProdId(rs.getInt("PROD_ID"));
                    product.setProdName(rs.getString("PROD_NAME"));
                    product.setProdPrice(rs.getDouble("PROD_PRICE"));
                    product.setQuantityStock(rs.getInt("QUANTITY_STOCK"));
                    product.setProdStatus(rs.getString("PROD_STATUS"));
                    product.setImagePath(rs.getString("IMAGE_PATH"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    // Update product status
    public void updateProductStatus(Product product) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PRODUCT_STATUS_QUERY)) {
            stmt.setString(1, product.getProdStatus());
            stmt.setInt(2, product.getProdId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
