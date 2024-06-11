package servicedesk.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // Database connection details
    private static final String DB_URL = "jdbc:derby:serviceDeskDB;create=true";
    private static final String DB_USERNAME = "admin";
    private static final String DB_PASSWORD = "admin";
    private Connection conn;
    private Statement statement;

    // Default constructor to create the connection and then create tables. 
    public DatabaseConnection() {
        try {
            // Attempt to make a connection 
            if (!connect()) {
                // Display error message if connection fails. 
                throw new SQLException("A connection to the database could not be established.");
            }
            // Create tables 
            createTables();
        } catch (SQLException ex) {
            // Handle SQL exceptions
            System.err.println("SQL Exception: " + ex.getMessage());
        }
    }

    // Method to connect to the database using connection information. 
    private boolean connect() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            statement = conn.createStatement();
            System.out.println("DATABASE: Connected"); // Success message
            return true;
        } catch (SQLException ex) {
            // Display error message and return false if connection failed. 
            System.err.println("SQL Exception: " + ex.getMessage());
            return false;
        }
    }

    // Create all required tables for the application 
    private void createTables() throws SQLException {
        createUsersTable();
        createTicketsTable();
        createCommentsTable();
        createTechnicianSpecialitiesTable();
    }

    // Create users table if it doesn't exist
    private void createUsersTable() throws SQLException {
        if (!tableExists("users")) {
            statement.executeUpdate("CREATE TABLE users ("
                    + "full_name VARCHAR(150), "
                    + "email VARCHAR(255) PRIMARY KEY, "
                    + "password VARCHAR(255), "
                    + "phone_number VARCHAR(20), "
                    + "department VARCHAR(50), "
                    + "role VARCHAR(50))");
        }
    }
    
    // Create ticket table if it doesn't exist 
    private void createTicketsTable() throws SQLException {
        if (!tableExists("tickets")) {
            statement.executeUpdate("CREATE TABLE tickets ("
                    + "ticket_id INT PRIMARY KEY, "
                    + "category VARCHAR(50), "
                    + "title VARCHAR(255), "
                    + "description VARCHAR(5000), "
                    + "priority VARCHAR(50), "
                    + "created_date TIMESTAMP, "
                    + "created_by_user_email VARCHAR(255), "
                    + "assigned_technician_email VARCHAR(255), "
                    + "resolved BOOLEAN, "
                    + "FOREIGN KEY (created_by_user_email) REFERENCES users(email), "
                    + "FOREIGN KEY (assigned_technician_email) REFERENCES users(email))");
        }
    }

    // Create comments table if it doesn't exist 
    private void createCommentsTable() throws SQLException {
        if (!tableExists("comments")) {
            statement.executeUpdate("CREATE TABLE comments ("
                    + "comment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                    + "ticket_id INT, "
                    + "timestamp TIMESTAMP, "
                    + "created_by_email VARCHAR(255), "
                    + "content VARCHAR(1024), "
                    + "FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id), "
                    + "FOREIGN KEY (created_by_email) REFERENCES users(email))");
        }
    }

    // Create technician_specialities table if it doesn't exist 
    private void createTechnicianSpecialitiesTable() throws SQLException {
        if (!tableExists("technician_specialities")) {
            statement.executeUpdate("CREATE TABLE technician_specialities ("
                    + "email VARCHAR(255), "
                    + "speciality VARCHAR(255), "
                    + "PRIMARY KEY (email, speciality))");
        }
    }

    // Check if a table exists in the database already.
    private boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet res = meta.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
            return res.next();
        }
    }

    // Getter to pass to other database files. 
    public Connection getConnection() {
        return conn;
    }
}
