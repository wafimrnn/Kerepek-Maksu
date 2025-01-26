package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.model.Drink;

public class DrinkDAO {
	
	// Create drink (assumes PROD_ID is already created in the Products table)
	public void insertDrink(Connection conn, Drink drink) throws SQLException {
	    String sql = "INSERT INTO DRINK (PROD_ID, VOLUME) VALUES (?, ?)";
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, drink.getProdId());
	        ps.setDouble(2, drink.getVolume());
	        ps.executeUpdate();
	    }
	}

}
