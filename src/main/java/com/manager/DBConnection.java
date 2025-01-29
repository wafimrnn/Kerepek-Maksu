package com.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Replace these with your Azure SQL Database details
    private static final String DB_URL = "jdbc:sqlserver://kerepekmaksu.windows.net:1433;database=KerepekMaksu;encrypt=true;trustServerCertificate=false;loginTimeout=30;";
    private static final String DB_USER = "maksuadmin";  // Replace with your Azure SQL username
    private static final String DB_PASSWORD = "Larva@12";  // Replace with your Azure SQL password

    static {
        try {
            // Load the JDBC driver for Microsoft SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get a connection to the Azure SQL Database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}