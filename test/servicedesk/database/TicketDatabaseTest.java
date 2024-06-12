package servicedesk.database;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import servicedesk.enums.Category;
import servicedesk.enums.Priority;
import servicedesk.models.Comment;
import servicedesk.models.Ticket;
import servicedesk.models.User;
import servicedesk.enums.Role;

// Testing class to see if methods in the TicketDatabase works.
public class TicketDatabaseTest {

    // Example Emails
    private static final String TECHNICIAN_EMAIL = "technician@example.com";
    private static final String USER_EMAIL = "user@example.com";

    @BeforeClass
    public static void setUp() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        UserDatabase userDB = new UserDatabase(dbConn.getConnection());

        // Create a new technician
        User technician = new User("Technician Name", TECHNICIAN_EMAIL, "technicianpassword", "1234567890", "IT", Role.TECHNICIAN);
        userDB.saveUser(technician);

        // Create a new user
        User user = new User("User Name", USER_EMAIL, "userpassword", "1234567890", "IT", Role.USER);
        userDB.saveUser(user);

        // Create a new ticket and assign it to the technician
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());
        ticketDB.insertTicket("Test Ticket", "Description", Category.SOFTWARE, Priority.MEDIUM, LocalDate.now(), USER_EMAIL, false, TECHNICIAN_EMAIL);
    }

    // Test to see if getUserTickets method works 
    @Test
    public void testGetUserTickets() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test with existing user
        List<Ticket> userTickets = ticketDB.getUserTickets(USER_EMAIL);
        assertNotNull("User tickets not retrieved.", userTickets);
        assertFalse("User tickets list is empty.", userTickets.isEmpty());
    }

    // Test if getCommentsForTicket method is working. 
    @Test
    public void testGetCommentsForTicket() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test with existing ticket
        List<Comment> comments = ticketDB.getCommentsForTicket(1);
        assertNotNull("Comments for ticket not retrieved.", comments);
        assertFalse("Comments list is empty.", comments.isEmpty());
    }

    // Test to see if insertTicket method works. 
    @Test
    public void testInsertTicket() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test inserting a new ticket
        ticketDB.insertTicket("Test Ticket", "Description", Category.SOFTWARE, Priority.MEDIUM, LocalDate.now(), USER_EMAIL, false, TECHNICIAN_EMAIL);
        List<Ticket> allTickets = ticketDB.getAllTickets();
        assertNotNull("Tickets not retrieved.", allTickets);
        assertFalse("Tickets list is empty.", allTickets.isEmpty());
    }

    // Test to see if getUserOpenTickets method works 
    @Test
    public void testGetUserOpenTickets() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test with existing user
        User user = new User("User Name", USER_EMAIL, "password", "1234567890", "IT", null);
        List<Ticket> openTickets = ticketDB.getUserOpenTickets(user);
        assertNotNull("User open tickets not retrieved.", openTickets);
        assertFalse("User open tickets list is empty.", openTickets.isEmpty());
    }

    // Test to see if getTicketsForTechnician method works. 
    @Test
    public void testGetTicketsForTechnician() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test with existing technician
        List<Ticket> technicianTickets = ticketDB.getTicketsForTechnician(TECHNICIAN_EMAIL);
        assertNotNull("Technician tickets not retrieved.", technicianTickets);
        assertFalse("Technician tickets list is empty.", technicianTickets.isEmpty());
    }

    // Test to make sure insertComment method works. 
    @Test
    public void testInsertComment() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test inserting a comment
        // NOTE: Linked to ticketID 1 by default
        ticketDB.insertComment(1, LocalDate.now().atStartOfDay(), USER_EMAIL, "Test Comment");
        List<Comment> comments = ticketDB.getCommentsForTicket(1);
        assertNotNull("Comments for ticket not retrieved.", comments);
        assertFalse("Comments list is empty.", comments.isEmpty());
    }

    // Test to make sure getAllTickets method works. 
    @Test
    public void testGetAllTickets() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test getting all tickets
        List<Ticket> allTickets = ticketDB.getAllTickets();
        assertNotNull("All tickets not retrieved.", allTickets);
        assertFalse("All tickets list is empty.", allTickets.isEmpty());
    }

    // Test to make sure getAvailableTickets works. 
    @Test
    public void testGetAvailableTickets() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test with technician specialties
        List<Category> specialties = List.of(Category.SOFTWARE, Category.NETWORK);
        List<Ticket> availableTickets = ticketDB.getAvailableTickets(specialties);
        assertNotNull("Available tickets not retrieved.", availableTickets);
        assertFalse("Available tickets list is empty.", availableTickets.isEmpty());
    }

    // Test to see if updateTicket method works. 
    @Test
    public void testUpdateTicket() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        TicketDatabase ticketDB = new TicketDatabase(dbConn.getConnection());

        // Test updating a ticket
        List<Ticket> userTickets = ticketDB.getUserTickets(USER_EMAIL);
        assertFalse("User tickets list is empty.", userTickets.isEmpty());
        Ticket ticket = userTickets.get(0);
        ticket.setDescription("Updated description");
        assertTrue("Ticket not updated.", ticketDB.updateTicket(ticket));
    }
}
