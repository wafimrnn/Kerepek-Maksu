package com.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.manager.DBConnection;
import com.model.Sale;
import com.model.SaleItem;

public class SaleDAO {

	public int insertSale(Sale sale) throws Exception {
	    String query = "INSERT INTO SALES (sale_Id, sale_Date, total_Amount, payment_Method, user_Id) VALUES (?, ?, ?, ?, ?)";
	    String getIdQuery = "SELECT SALES_SEQ.NEXTVAL FROM DUAL"; // Assuming SALES_SEQ is your sequence name

	    try (Connection conn = DBConnection.getConnection()) {
	        // Get the next sequence value
	        int saleId;
	        try (PreparedStatement psGetId = conn.prepareStatement(getIdQuery);
	             ResultSet rs = psGetId.executeQuery()) {
	            if (rs.next()) {
	                saleId = rs.getInt(1);
	            } else {
	                throw new Exception("Failed to retrieve the next value from SALES_SEQ.");
	            }
	        }

	        // Insert the sale record
	        try (PreparedStatement ps = conn.prepareStatement(query)) {
	            ps.setInt(1, saleId); // Set the primary key
	            ps.setDate(2, Date.valueOf(sale.getSaleDate()));
	            ps.setDouble(3, sale.getTotalAmount());
	            ps.setString(4, sale.getPaymentMethod());
	            ps.setInt(5, sale.getUserId());
	            ps.executeUpdate();
	        }

	        return saleId; // Return the generated saleId
	    }
	}

    public void insertSaleItems(Connection conn, int saleId, List<SaleItem> saleItems) throws SQLException {
        String query = "INSERT INTO SaleItems (sale_Id, prod_Id, quantity, sub_Total) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (SaleItem item : saleItems) {
                // Avoid duplicate inserts
                if (!isSaleItemExists(conn, saleId, item.getProdId())) {
                    ps.setInt(1, saleId);
                    ps.setInt(2, item.getProdId());
                    ps.setInt(3, item.getQuantity());
                    ps.setDouble(4, item.getSubTotal());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private boolean isSaleItemExists(Connection conn, int saleId, int prodId) throws SQLException {
        String query = "SELECT COUNT(*) FROM SaleItems WHERE sale_Id = ? AND prod_Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, saleId);
            ps.setInt(2, prodId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    public List<Sale> getSalesByDate(Date saleDate) {
        List<Sale> salesList = new ArrayList<>();
        String query = "SELECT sale_Id, sale_Date, total_Amount, payment_Method, user_Id FROM Sales WHERE SALE_DATE = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, new java.sql.Date(saleDate.getTime()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Sale sale = new Sale();
                    sale.setSaleId(resultSet.getInt("sale_Id"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    sale.setSaleDate(dateFormat.format(resultSet.getDate("sale_Date")));
                    sale.setTotalAmount(resultSet.getBigDecimal("total_Amount").doubleValue());
                    sale.setPaymentMethod(resultSet.getString("payment_Method"));
                    sale.setUserId(resultSet.getInt("user_Id"));
                    salesList.add(sale);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesList;
    }

    
    public boolean checkSaleItemExists(int saleId, int prodId) throws Exception {
        // Updated to match the schema (SALE_ID and PROD_ID)
        String query = "SELECT COUNT(*) FROM SaleItems WHERE SALE_ID = ? AND PROD_ID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, saleId);
            ps.setInt(2, prodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


}
