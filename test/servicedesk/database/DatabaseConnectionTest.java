package servicedesk.database;

import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.SQLException;

// Test to make sure database connects and the tables exist
public class DatabaseConnectionTest {

    // Test the connection to the database 
    @Test
    public void testConnect() throws SQLException {
        System.out.println("Testing database connection.");
        DatabaseConnection instance = new DatabaseConnection();
        assertTrue("Database connection failed.", instance.connect());
        System.out.println("Database connected successfully.");
    }

    // Testing the existence of the users table 
    @Test
    public void testUsersTableExists() throws SQLException {
        System.out.println("Testing users table existence.");
        DatabaseConnection instance = new DatabaseConnection();
        assertTrue("Users table does not exist.", instance.tableExists("users"));
        System.out.println("Users table exists.");
    }

    // Testing the existence of the tickets table 
    @Test
    public void testTicketsTableExists() throws SQLException {
        System.out.println("Testing tickets table existence.");
        DatabaseConnection instance = new DatabaseConnection();
        assertTrue("Tickets table does not exist.", instance.tableExists("tickets"));
        System.out.println("Tickets table exists.");
    }

    // Testing the existence of the comments table 
    @Test
    public void testCommentsTableExists() throws SQLException {
        System.out.println("Testing comments table existence.");
        DatabaseConnection instance = new DatabaseConnection();
        assertTrue("Comments table does not exist.", instance.tableExists("comments"));
        System.out.println("Comments table exists.");
    }

    // Testing the existence of the technician_specialities table 
    @Test
    public void testTechnicianSpecialitiesTableExists() throws SQLException {
        System.out.println("Testing technician specialities table existence...");
        DatabaseConnection instance = new DatabaseConnection();
        assertTrue("Technician specialities table does not exist.", instance.tableExists("technician_specialities"));
        System.out.println("Technician specialities table exists.");
    }
}
