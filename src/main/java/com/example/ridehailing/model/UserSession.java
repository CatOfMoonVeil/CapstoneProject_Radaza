package com.example.ridehailing.model;

import com.example.ridehailing.util.DatabaseConnection;
import com.example.ridehailing.util.PasswordHasher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSession {
    private static Passenger loggedInPassenger;

    public static Passenger getLoggedInPassenger() {
        return loggedInPassenger;
    }

    public static void setLoggedInPassenger(Passenger passenger) {
        loggedInPassenger = passenger;
    }

    // Dynamic Login checking encrypted passwords against database
    public static Passenger login(String email, String password) {
        String query = "SELECT u.user_id, p.passenger_id, p.name, p.phone, u.email " +
                "FROM users u JOIN passengers p ON u.user_id = p.user_id " +
                "WHERE u.email = ? AND u.password = ? AND u.role = 'PASSENGER'";

        String hashedPassword = PasswordHasher.hashPassword(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Passenger(
                            rs.getInt("passenger_id"), // Uses passengers table primary key
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database login system error: " + e.getMessage());
        }
        return null;
    }

    // Register Passenger (Using SQL transactions across 2 tables)
    public static void registerPassenger(String name, String phone, String email, String password) throws SQLException {
        String insertUserQuery = "INSERT INTO users (email, password, role) VALUES (?, ?, 'PASSENGER')";
        String insertPassengerQuery = "INSERT INTO passengers (user_id, name, phone) VALUES (?, ?, ?)";

        String hashedPassword = PasswordHasher.hashPassword(password);
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start database transaction

            // 1. Insert into base users table
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, email);
                userStmt.setString(2, hashedPassword);
                userStmt.executeUpdate();

                ResultSet rs = userStmt.getGeneratedKeys();
                int userId = -1;
                if (rs.next()) {
                    userId = rs.getInt(1);
                }

                // 2. Insert into passengers table
                try (PreparedStatement passStmt = conn.prepareStatement(insertPassengerQuery)) {
                    passStmt.setInt(1, userId);
                    passStmt.setString(2, name);
                    passStmt.setString(3, phone);
                    passStmt.executeUpdate();
                }
            }
            conn.commit(); // Transaction successful
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    // Register Driver (Using SQL transactions across 2 tables)
    public static void registerDriver(String name, String phone, String email, String password, String vehicle) throws SQLException {
        String insertUserQuery = "INSERT INTO users (email, password, role) VALUES (?, ?, 'DRIVER')";
        String insertDriverQuery = "INSERT INTO drivers (user_id, name, phone, vehicle, status) VALUES (?, ?, ?, ?, 'Available')";

        String hashedPassword = PasswordHasher.hashPassword(password);
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start database transaction

            // 1. Insert into base users table
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, email);
                userStmt.setString(2, hashedPassword);
                userStmt.executeUpdate();

                ResultSet rs = userStmt.getGeneratedKeys();
                int userId = -1;
                if (rs.next()) {
                    userId = rs.getInt(1);
                }

                // 2. Insert into drivers table
                try (PreparedStatement driveStmt = conn.prepareStatement(insertDriverQuery)) {
                    driveStmt.setInt(1, userId);
                    driveStmt.setString(2, name);
                    driveStmt.setString(3, phone);
                    driveStmt.setString(4, vehicle);
                    driveStmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    // Dynamically queries the database for an available driver
    public static Driver fetchAvailableDriver() {
        String query = "SELECT * FROM drivers WHERE status = 'Available' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new Driver(
                        rs.getInt("driver_id"), // Unique driver table ID!
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("vehicle")
                );
            }
        } catch (SQLException e) {
            System.err.println("Failed to look up driver profile: " + e.getMessage());
        }
        return null;
    }
}