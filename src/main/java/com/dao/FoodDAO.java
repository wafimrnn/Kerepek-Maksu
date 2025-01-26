package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.model.Food;

public class FoodDAO {

    // Create food (assumes PROD_ID is already created in the Products table)
	public void insertFood(Connection conn, Food food) throws SQLException {
	    String sql = "INSERT INTO FOOD (PROD_ID, PACKAGING_TYPE, WEIGHT) VALUES (?, ?, ?)";
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, food.getProdId());
	        ps.setString(2, food.getPackagingType());
	        ps.setDouble(3, food.getWeight());
	        ps.executeUpdate();
	    }
	}
}
