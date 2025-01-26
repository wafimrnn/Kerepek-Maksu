package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DrinkDAO {
	
	//create drink
    public void insertDrink(Connection conn, int prodId, double volume) throws SQLException {
        String sql = "INSERT INTO Drink (PROD_ID, VOLUME) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prodId);
            ps.setDouble(2, volume);
            ps.executeUpdate();
        }
    }
}