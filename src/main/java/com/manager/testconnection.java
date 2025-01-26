package com.manager;

import java.sql.Connection;
import java.sql.SQLException;

public class testconnection {
	public static void main(String[] args) {
	    try {
	        Connection conn = DBConnection.getConnection();
	        System.out.println("Connected to Oracle Database!");
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
