package com.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Replace these with your Oracle Express 11g database details
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Default connection string for Oracle Express (XE)
    private static final String DB_USER = "KerepekMaksu";  // Replace with your Oracle username
    private static final String DB_PASSWORD = "system";  // Replace with your Oracle password
    
    static {
        try {
            // Load the JDBC driver for Oracle
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
