package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import com.manager.DBConnection;
import com.model.Sale;
import com.model.SaleItem;

public class SaleDAO {

	// Insert a new sale and return the generated sale ID
	// Insert a new sale and return the generated sale ID
	public int insertSale(Sale sale) throws SQLException {
	    String getIdSQL = "SELECT SALE_SEQ.NEXTVAL FROM DUAL"; // Replace SALE_SEQ with your actual sequence name
	    String insertSaleSQL = "INSERT INTO SALES (SALE_ID, SALE_DATE, TOTAL_AMOUNT, PAYMENT_METHOD, USER_ID) VALUES (?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DBConnection.getConnection()) {
	        // Start a transaction
	        conn.setAutoCommit(false);
	        int saleId;

	        try (PreparedStatement getIdStmt = conn.prepareStatement(getIdSQL);
	             PreparedStatement insertSaleStmt = conn.prepareStatement(insertSaleSQL)) {

	            // Fetch the next value from the sequence
	            try (ResultSet rs = getIdStmt.executeQuery()) {
	                if (rs.next()) {
	                    saleId = rs.getInt(1);
	                } else {
	                    throw new SQLException("Failed to fetch next value from sequence.");
	                }
	            }

	            // Convert sale date (String) to java.sql.Date
	            java.sql.Date sqlDate = convertStringToSQLDate(sale.getSaleDate());

	            // Insert the sale
	            insertSaleStmt.setInt(1, saleId); // Set the fetched SALE_ID
	            insertSaleStmt.setDate(2, sqlDate);
	            insertSaleStmt.setDouble(3, sale.getTotalAmount());
	            insertSaleStmt.setString(4, sale.getPaymentMethod());
	            insertSaleStmt.setInt(5, sale.getUserId());
	            insertSaleStmt.executeUpdate();

	            // Correctly pass all arguments to insertSaleItems
	            insertSaleItems(conn, saleId, sale.getSaleItems());

	            // Commit transaction
	            conn.commit();
	            return saleId;
	        } catch (SQLException e) {
	            // Rollback in case of error
	            conn.rollback();
	            throw e;
	        }
	    }
	}

    // Insert sale items into SALEITEMS table with the given saleId
	public void insertSaleItems(Connection conn, int saleId, List<SaleItem> saleItems) throws SQLException {
	    String sql = "INSERT INTO SALEITEMS (SALE_ID, PROD_ID, QUANTITY, SUB_TOTAL) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        for (SaleItem item : saleItems) {
	            stmt.setInt(1, saleId);  // Use the generated SALE_ID
	            stmt.setInt(2, item.getProdId());
	            stmt.setInt(3, item.getQuantity());
	            stmt.setDouble(4, item.getSubTotal());
	            stmt.addBatch(); // Add each insert statement to batch
	        }
	        stmt.executeBatch(); // Execute all insertions as a batch
	    }
	}

    // Utility method to convert String to java.sql.Date
    private java.sql.Date convertStringToSQLDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Match the format of the date string
            java.util.Date utilDate = sdf.parse(dateStr); // Parse String to java.util.Date
            return new java.sql.Date(utilDate.getTime()); // Convert to java.sql.Date
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Expected format is yyyy-MM-dd. Provided: " + dateStr, e);
        }
    }
}
