package servicedesk.database;

import servicedesk.enums.Category;
import servicedesk.enums.Role;
import servicedesk.models.User;
import servicedesk.PasswordHasher;

import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;

// Testing class to see if methods in the UserDatabase works.
public class UserDatabaseTest {

    // Test to save a new user 
    @Test
    public void testSaveUser() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test saving a new user
        User newUser = new User("Mike Wilson", "mike@example.com", PasswordHasher.hashPassword("password"), "1234567890", "IT", Role.USER);
        assertTrue("Failed to save new user.", userDB.saveUser(newUser));

    }

    // Test to see if the userExists method works. 
    @Test
    public void testUserExists() {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test with an existing user
        assertTrue("Existing user not found.", userDB.userExists("mike@example.com"));

        // Test with a non-existing user
        assertFalse("Non-existing user found.", userDB.userExists("nonexisting@example.com"));
    }

    // Testing to see if the authenticateUser method works. 
    @Test
    public void testAuthenticateUser() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test with valid credentials
        User validUser = userDB.authenticateUser("mike@example.com", PasswordHasher.hashPassword("password"));
        assertNotNull("Valid user authentication failed.", validUser);

        // Test with invalid credentials
        User invalidUser = userDB.authenticateUser("nonexisting@example.com", PasswordHasher.hashPassword("wrongpassword"));
        assertNull("Invalid user authenticated.", invalidUser);
    }

    // Testing to see if the getTechnicianSpecialties method works. 
    @Test
    public void testGetTechnicianSpecialties() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test with existing technician
        List<Category> specialties = userDB.getTechnicianSpecialties("mike@example.com");
        assertNotNull("Technician's specialties not retrieved.", specialties);
        assertFalse("Technician's specialties list is empty.", specialties.isEmpty());

        // Test with non-existing technician
        List<Category> nonExistingSpecialties = userDB.getTechnicianSpecialties("nonexisting@example.com");
        assertNotNull("Non-existing technician's specialties not retrieved.", nonExistingSpecialties);
        assertTrue("Non-existing technician's specialties list is not empty.", nonExistingSpecialties.isEmpty());
    }

    // Testing if the getUserByEmail method works. 
    @Test
    public void testGetUserByEmail() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test with existing user
        User existingUser = userDB.getUserByEmail("mike@example.com");
        assertNull("Existing user not retrieved.", existingUser);

        // Test with non-existing user
        User nonExistingUser = userDB.getUserByEmail("nonexisting@example.com");
        assertNull("Non-existing user retrieved.", nonExistingUser);
    }

    // Test that the getAllUsers method works.
    @Test
    public void testGetAllUsers() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        List<User> allUsers = userDB.getAllUsers();
        assertNotNull("All users were not retrieved.", allUsers);
        assertFalse("User list is empty.", allUsers.isEmpty());
    }

    // Test if the updateUserRole method works
    @Test
    public void testUpdateUserRole() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test updating user role
        userDB.updateUserRole("mike@example.com", Role.TECHNICIAN);

        // Check if the role is updated
        User updatedUser = userDB.getUserByEmail("existing@example.com");
        assertNotNull("User role was not updated.", updatedUser);
        assertEquals("User role not updated to Technician.", Role.TECHNICIAN, updatedUser.getRole());
    }

    // Test if updateUserPassword method works. 
    @Test
    public void testUpdateUserPassword() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test updating user password
        assertTrue("User password not updated.", userDB.updateUserPassword("mike@example.com", PasswordHasher.hashPassword("newpassword")));

        // Authenticate with the new password
        User authenticatedUser = userDB.authenticateUser("mike@example.com", PasswordHasher.hashPassword("newpassword"));
        assertNotNull("User authentication with new password failed.", authenticatedUser);
    }

    // Test if updateUserSpecialties method works. 
    @Test
    public void testUpdateUserSpecialties() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Test updating user specialties
        List<Category> newSpecialties = List.of(Category.SOFTWARE, Category.NETWORK);
        assertTrue("User specialties was not updated.", userDB.updateUserSpecialties("mike@example.com", newSpecialties));

        // Check if the specialties are updated
        List<Category> updatedSpecialties = userDB.getTechnicianSpecialties("mike@example.com");
        assertNotNull("User specialties not retrieved.", updatedSpecialties);
        assertFalse("User specialties list is empty.", updatedSpecialties.isEmpty());
        assertTrue("User specialties not updated.", updatedSpecialties.containsAll(newSpecialties));
    }
}
