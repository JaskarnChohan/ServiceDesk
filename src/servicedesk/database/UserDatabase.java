package servicedesk.database;

import servicedesk.enums.Role;
import servicedesk.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import servicedesk.enums.Category;

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

    // Method to authenticate the user using email and hashed password. Used uring login. 
    public User authenticateUser(String email, String hashedPassword) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
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

    // Mathod to get a technician's specialties. 
    public List<Category> getTechnicianSpecialities(String email) {
        List<Category> specialties = new ArrayList<>();
        String sql = "SELECT speciality FROM technician_specialities WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String specialtyName = rs.getString("speciality");
                    Category specialty = Category.valueOf(specialtyName);
                    specialties.add(specialty);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return specialties;
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

    // Method to get all users from the database. 
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String hashedPassword = rs.getString("password");
                String phoneNumber = rs.getString("phone_number");
                String department = rs.getString("department");
                Role role = Role.valueOf(rs.getString("role"));

                User user = new User(fullName, email, hashedPassword, phoneNumber, department, role);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    // Method to update a user's role.
    public void updateUserRole(String email, Role role) throws SQLException {
        String updateRoleSQL = "UPDATE users SET role = ? WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateRoleSQL)) {
            pstmt.setString(1, role.name());
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        }
    }

    // Method to update a user's password. 
    public boolean updateUserPassword(String email, String hashedPassword) {
        String updatePasswordSQL = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updatePasswordSQL)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, email);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to update a user's specialties.  
    public boolean updateUserSpecialties(String email, List<Category> specialties) {
        String deleteSQL = "DELETE FROM technician_specialities WHERE email = ?";
        String insertSQL = "INSERT INTO technician_specialities (email, speciality) VALUES (?, ?)";

        try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSQL)) {
            // Begin transaction
            conn.setAutoCommit(false);

            // Delete existing specialties
            deletePstmt.setString(1, email);
            deletePstmt.executeUpdate();

            // Insert new specialties
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSQL)) {
                for (Category speciality : specialties) {
                    insertPstmt.setString(1, email);
                    insertPstmt.setString(2, speciality.name());
                    insertPstmt.addBatch();
                }
                insertPstmt.executeBatch();
            }

            // Commit transaction
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }

}
