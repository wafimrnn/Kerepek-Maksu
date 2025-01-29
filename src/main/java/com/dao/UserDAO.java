package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.manager.DBConnection;
import com.model.User;

public class UserDAO {

    // Method to create an owner
    public boolean createOwner(String username, String password, String phone, String address) {
    	String sql = "INSERT INTO USERS (USER_ID, USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS) VALUES (USERS_SEQ.NEXTVAL, ?, 'OWNER', ?, ?, ?, 'ACTIVE')";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);  // Store plain-text password
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating owner: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Method to log in a user
    public User loginUser(String username, String password) {
        // Update the query to match the database schema
        String query = "SELECT * FROM Users WHERE USER_NAME = ? AND USER_PASS = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username); // Bind username to USER_NAME
            statement.setString(2, password); // Bind password to USER_PASS

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("USER_ID")); // Match USER_ID column
                user.setName(resultSet.getString("USER_NAME")); // Match USER_NAME column
                user.setRole(resultSet.getString("USER_ROLE")); // Match USER_ROLE column
                return user; // Return the user object
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no user is found
    }


    // Method to create a staff account
    public boolean createStaff(String username, String password, String phone, String address, int ownerId) {
    	String query = "INSERT INTO USERS (USER_ID, USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS, OWNER_ID) " +
                "VALUES (USERS_SEQ.NEXTVAL, ?, 'STAFF', ?, ?, ?, 'ACTIVE', ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);  // Hash the password before storing
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, ownerId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating staff: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    // Method to check if the username is already taken
    public boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM USERS WHERE USER_NAME = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if the count is greater than 0
            }
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Method to update a user's account
    public boolean updateUserAccount(int userId, String username, String phone, String address, String role, String status) {
        String sql = "UPDATE USERS SET USER_NAME = ?, USER_PHONE = ?, USER_ADDRESS = ?, USER_ROLE = ?, ACC_STATUS = ? WHERE USER_ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setString(4, role);
            stmt.setString(5, status);
            stmt.setInt(6, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user account: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Method to get a user by their ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setRole(rs.getString("USER_ROLE"));
                user.setPhone(rs.getString("USER_PHONE"));
                user.setAddress(rs.getString("USER_ADDRESS"));
                user.setAccStatus(rs.getString("ACC_STATUS"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Return null if user not found
    }
    
 // Method to get staff members by owner ID
    public List<User> getStaffByOwnerId(int ownerId) {
        String sql = "SELECT * FROM USERS WHERE OWNER_ID = ? AND USER_ROLE = 'STAFF'";
        List<User> staffList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setRole(rs.getString("USER_ROLE"));
                user.setPhone(rs.getString("USER_PHONE"));
                user.setAddress(rs.getString("USER_ADDRESS"));
                staffList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving staff by owner ID: " + e.getMessage());
            e.printStackTrace();
        }
        return staffList;
    }
    
    public boolean updateAccountStatus(int staffId, String newStatus) {
        String sql = "UPDATE USERS SET ACC_STATUS = ? WHERE USER_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, staffId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
