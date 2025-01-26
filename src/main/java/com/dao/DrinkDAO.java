package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DrinkDAO {
	
	// Create drink (assumes PROD_ID is already created in the Products table)
    public void insertDrink(Connection conn, double volume) throws SQLException {
        // Fetch PROD_ID from the last inserted product
        String getProdIdSql = "SELECT PRODUCTS_SEQ.CURRVAL FROM DUAL";  // Assuming PROD_SEQ is used for generating PROD_ID

        int prodId = 0;
        try (PreparedStatement ps = conn.prepareStatement(getProdIdSql)) {
            var rs = ps.executeQuery();
            if (rs.next()) {
                prodId = rs.getInt(1);
            }
        }

        // Insert into Drink table with the fetched PROD_ID
        String sql = "INSERT INTO Drink (PROD_ID, VOLUME) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prodId);  // Use the fetched PROD_ID
            ps.setDouble(2, volume);
            ps.executeUpdate();
        }
    }
}
