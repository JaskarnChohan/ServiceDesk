package servicedesk.models;

import servicedesk.enums.Role;
import servicedesk.PasswordHasher;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// Testing the User class 
public class UserTest {

    private User user;

    @Before
    public void setUp() {
        // Create a user object for testing
        user = new User("Mike Wilson", "mike@example.com", "hashedPassword", "1234567890", "IT", Role.USER);
    }

    @Test
    public void testGetFullName() {
        assertEquals("Mike Wilson", user.getFullName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("mike@example.com", user.getEmail());
    }

    @Test
    public void testGetHashedPassword() {
        assertEquals("hashedPassword", user.getHashedPassword());
    }

    @Test
    public void testGetPhoneNumber() {
        assertEquals("1234567890", user.getPhoneNumber());
    }

    @Test
    public void testGetDepartment() {
        assertEquals("IT", user.getDepartment());
    }

    @Test
    public void testGetRole() {
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void testSetRole() {
        user.setRole(Role.TECHNICIAN);
        assertEquals(Role.TECHNICIAN, user.getRole());
    }

    @Test
    public void testSetHashedPassword() {
        user.setHashedPassword("newHashedPassword");
        assertEquals("newHashedPassword", user.getHashedPassword());
    }

    @Test
    public void testToString() {
        assertEquals("mike@example.com  (USER)", user.toString());
    }

    @Test
    public void testHashPassword() {
        // Test hashing of password using PasswordHasher class
        String hashedPassword = PasswordHasher.hashPassword("password123");
        assertNotNull(hashedPassword);
        assertNotEquals("password123", hashedPassword); // Ensure hashed password is not the same as original
    }
}
