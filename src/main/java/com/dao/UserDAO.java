package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.manager.DBConnection;
import com.model.User;

public class UserDAO {

    // Method to create an owner (without hashing password)
    public boolean createOwner(String username, String password, String phone, String address) {
        String sql = "INSERT INTO USERS (USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS) VALUES (?, 'OWNER', ?, ?, ?, 'ACTIVE')";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Store the password as plain text
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);  // Store plain text password
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace(); // Log the error for debugging
        }
        return false;
    }

    // Method to log in a user (compare passwords directly)
    public User loginUser(String username, String password) {
        String query = "SELECT * FROM USERS WHERE USER_NAME = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("USER_PASS");  // Get plain text password

                // Direct comparison of the password (no hashing)
                if (password.equals(storedPassword)) {
                    User user = new User();
                    user.setId(rs.getInt("USER_ID"));
                    user.setName(rs.getString("USER_NAME"));
                    user.setRole(rs.getString("USER_ROLE"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no match is found
    }

    // Method to create a staff account (without hashing password)
    public boolean createStaff(String username, String password, String phone, String address, int ownerId) {
        String query = "INSERT INTO USERS (USER_NAME, USER_ROLE, USER_PASS, USER_PHONE, USER_ADDRESS, ACC_STATUS, OWNER_ID) VALUES (?, 'STAFF', ?, ?, ?, 'ACTIVE', ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);  // Store plain text password
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, ownerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
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
            e.printStackTrace();
        }
        return false;
    }

    // Method to update user account (no changes needed here, this doesn't involve password)
    private static final String UPDATE_ACCOUNT_QUERY = "UPDATE USERS SET USER_PHONE = ?, USER_ADDRESS = ? WHERE USER_ID = ?";
    public boolean updateUserAccount(int userId, String phone, String address) {
        boolean isUpdated = false;
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT_QUERY);
            statement.setString(1, phone);
            statement.setString(2, address);
            statement.setInt(3, userId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                isUpdated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    // Method to get a user by their ID (no changes needed here)
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setName(rs.getString("user_name"));
                    user.setRole(rs.getString("user_role"));
                    user.setPhone(rs.getString("user_phone"));
                    user.setAddress(rs.getString("user_address"));
                    return user;
                }
            }
        }
        return null; // Return null if user is not found
    }
}
