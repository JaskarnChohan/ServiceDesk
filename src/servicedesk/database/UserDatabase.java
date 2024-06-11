package servicedesk.database;

import servicedesk.enums.Role;
import servicedesk.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Method to store all database methods revelant to the user/technician. 
public class UserDatabase {

    private Connection conn;

    // Default contructor to get the connection 
    public UserDatabase(Connection conn) {
        this.conn = conn;
    }

    // Method to check the database if the user exists. Used during signup 
    public boolean userExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next(); // If there's a result then it means that a user exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to save a user into the database with the User object. 
    public boolean saveUser(User user) {
        String insertUserSQL = "INSERT INTO users (full_name, email, password, phone_number, department, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getHashedPassword());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getDepartment());
            pstmt.setString(6, user.getRole().name());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get a user's information using their email. 
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
                    String hashedPassword = rs.getString("password");
                    String phoneNumber = rs.getString("phone_number");
                    String department = rs.getString("department");
                    Role role = Role.valueOf(rs.getString("role"));
                    return new User(fullName, email, hashedPassword, phoneNumber, department, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
