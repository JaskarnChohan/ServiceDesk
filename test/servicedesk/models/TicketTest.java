package servicedesk.models;

import servicedesk.enums.Category;
import servicedesk.enums.Priority;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Testing the Ticket class 
public class TicketTest {

    private Ticket ticket;
    private List<Comment> comments;

    // Initial setup to make the test work 
    @Before
    public void setUp() {
        // Create some comments for testing
        comments = new ArrayList<>();
        comments.add(new Comment(1, LocalDateTime.now(), "mike@example.com", "Test comment 1"));
        comments.add(new Comment(1, LocalDateTime.now(), "mike@example.com", "Test comment 2"));

        // Create a ticket object for testing
        ticket = new Ticket(1, Category.HARDWARE, "Test Ticket", "Description", Priority.HIGH, LocalDate.now(), "mike@example.com", null, false, comments);
    }

    // Testing the getters 
    @Test
    public void testGetters() {
        assertEquals(1, ticket.getTicketId());
        assertEquals(Category.HARDWARE, ticket.getCategory());
        assertEquals("Test Ticket", ticket.getTitle());
        assertEquals("Description", ticket.getDescription());
        assertEquals(Priority.HIGH, ticket.getPriority());
        assertEquals(LocalDate.now(), ticket.getCreatedDate());
        assertEquals("mike@example.com", ticket.getCreatedByEmail());
        assertFalse(ticket.isResolved());
        assertNull(ticket.getAssignedTechnicianEmail());
        assertEquals(comments, ticket.getComments());
    }

    // Testing to change the resolved status 
    @Test
    public void testChangeResolvedStatus() {
        // Ticket should not be resolved initally
        assertFalse(ticket.isResolved());

        // Change the resolved status to true
        ticket.setResolved(true);
        assertTrue(ticket.isResolved());

        // Change the resolved status back to false
        ticket.setResolved(false);
        assertFalse(ticket.isResolved());
    }

    // Testing adding comment 
    @Test
    public void testAddComment() {
        // Should only be 2 comments which we added at the start. 
        assertEquals(2, ticket.getComments().size());

        // Add a new comment
        // NOTE: 1 is default ticketID. 
        Comment newComment = new Comment(1, LocalDateTime.now(), "mike@example.com", "New comment");
        ticket.addComment(newComment);

        // Now there should be three comments
        assertEquals(3, ticket.getComments().size());
        assertTrue(ticket.getComments().contains(newComment));
    }

    // Testing assigned technician email 
    @Test
    public void testAssignedTechnicianEmail() {
        // Assigned technician email should be null 
        assertNull(ticket.getAssignedTechnicianEmail());

        // Assign a technician email
        ticket.setAssignedTechnicianEmail("technician@example.com");

        // Now it should not be null
        assertNotNull(ticket.getAssignedTechnicianEmail());
        assertEquals("technician@example.com", ticket.getAssignedTechnicianEmail());
    }
    
    // Testing the toString() which is displayed in drop down boxes. 
    @Test
    public void testToString() {
        String expectedToString = "Ticket ID: 1 - Test Ticket";
        assertEquals(expectedToString, ticket.toString());
    }

}
