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


    public static String checkUserRoleAndLogin(String email, String password) {
        String query = "SELECT role FROM users WHERE email = ? AND password = ?";
        String hashedPassword = PasswordHasher.hashPassword(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.err.println("Role check error: " + e.getMessage());
        }
        return null;
    }


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
                            rs.getInt("passenger_id"),
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

    public static void registerPassenger(String name, String phone, String email, String password) throws SQLException {
        String insertUserQuery = "INSERT INTO users (email, password, role) VALUES (?, ?, 'PASSENGER')";
        String insertPassengerQuery = "INSERT INTO passengers (user_id, name, phone) VALUES (?, ?, ?)";

        String hashedPassword = PasswordHasher.hashPassword(password);
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, email);
                userStmt.setString(2, hashedPassword);
                userStmt.executeUpdate();

                ResultSet rs = userStmt.getGeneratedKeys();
                int userId = -1;
                if (rs.next()) {
                    userId = rs.getInt(1);
                }

                try (PreparedStatement passStmt = conn.prepareStatement(insertPassengerQuery)) {
                    passStmt.setInt(1, userId);
                    passStmt.setString(2, name);
                    passStmt.setString(3, phone);
                    passStmt.executeUpdate();
                }
            }
            conn.commit(); // Save changes
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    /**
     * Registers a new driver across 'users' and 'drivers' tables using a Transaction.
     */
    public static void registerDriver(String name, String phone, String email, String password, String vehicle) throws SQLException {
        String insertUserQuery = "INSERT INTO users (email, password, role) VALUES (?, ?, 'DRIVER')";
        String insertDriverQuery = "INSERT INTO drivers (user_id, name, phone, vehicle, status) VALUES (?, ?, ?, ?, 'Available')";

        String hashedPassword = PasswordHasher.hashPassword(password);
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, email);
                userStmt.setString(2, hashedPassword);
                userStmt.executeUpdate();

                ResultSet rs = userStmt.getGeneratedKeys();
                int userId = -1;
                if (rs.next()) {
                    userId = rs.getInt(1);
                }

                try (PreparedStatement driveStmt = conn.prepareStatement(insertDriverQuery)) {
                    driveStmt.setInt(1, userId);
                    driveStmt.setString(2, name);
                    driveStmt.setString(3, phone);
                    driveStmt.setString(4, vehicle);
                    driveStmt.executeUpdate();
                }
            }
            conn.commit(); // Save changes
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }


    public static Driver fetchAvailableDriver() {
        String query = "SELECT * FROM drivers WHERE status = 'Available' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new Driver(
                        rs.getInt("driver_id"),
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