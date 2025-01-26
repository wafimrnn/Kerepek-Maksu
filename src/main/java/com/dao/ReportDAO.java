package com.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.manager.DBConnection;
import com.model.SalesReport;

public class ReportDAO {
    public List<SalesReport> getSalesReport(Date startDate, Date endDate) throws SQLException {
        String query = "SELECT S.SALE_ID, S.SALE_DATE, U.USER_NAME, P.PROD_NAME, SI.QUANTITY, SI.SUB_TOTAL, S.TOTAL_AMOUNT, S.PAYMENT_METHOD " +
                       "FROM SALES S " +
                       "JOIN SALEITEMS SI ON S.SALE_ID = SI.SALE_ID " +
                       "JOIN PRODUCTS P ON SI.PROD_ID = P.PROD_ID " +
                       "JOIN USERS U ON S.USER_ID = U.USER_ID " +
                       "WHERE S.SALE_DATE BETWEEN ? AND ? " +
                       "ORDER BY S.SALE_DATE";

        List<SalesReport> salesReports = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); // Implement your connection logic
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SalesReport report = new SalesReport(
                    rs.getInt("SALE_ID"),
                    rs.getDate("SALE_DATE"),
                    rs.getString("USER_NAME"),
                    rs.getString("PROD_NAME"),
                    rs.getInt("QUANTITY"),
                    rs.getBigDecimal("SUB_TOTAL"),
                    rs.getBigDecimal("TOTAL_AMOUNT"),
                    rs.getString("PAYMENT_METHOD")
                );
                salesReports.add(report);
            }
        }
        return salesReports;
    }
}