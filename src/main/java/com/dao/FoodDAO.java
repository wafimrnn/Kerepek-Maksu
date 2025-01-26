package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FoodDAO {
	
	//create food
    public void insertFood(Connection conn, int prodId, String packagingType, double weight) throws SQLException {
        String sql = "INSERT INTO Food (PROD_ID, PACKAGING_TYPE, WEIGHT) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prodId);
            ps.setString(2, packagingType);
            ps.setDouble(3, weight);
            ps.executeUpdate();
        }
    }
}
