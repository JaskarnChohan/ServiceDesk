package servicedesk.models;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// Testing the Ticket class 
public class CommentTest {

    private Comment comment;
    private LocalDateTime fixedTimestamp;

    // Initial setup to make the test work
    @Before
    public void setUp() {
        // Create a fixed time stap for testing
        fixedTimestamp = LocalDateTime.of(2024, 6, 12, 0, 0);
        // Create a comment object for testing
        comment = new Comment(1, fixedTimestamp, "user@example.com", "Test comment");
    }

    // Testing the getters 
    @Test
    public void testGetTimestamp() {
        assertEquals(fixedTimestamp, comment.getTimestamp());
    }

    @Test
    public void testGetContent() {
        assertEquals("Test comment", comment.getContent());
    }

    @Test
    public void testGetCreatedByEmail() {
        assertEquals("user@example.com", comment.getCreatedByEmail());
    }

    // Testing the toString() to see if it matches 
    @Test
    public void testToString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Format the timestamp of the comment using the formatter
        String expectedTimestamp = formatter.format(comment.getTimestamp());

        // Construct the expected string with the formatted timestamp
        String expectedToString = "[" + expectedTimestamp + "] user@example.com: Test comment";

        assertEquals(expectedToString, comment.toString());
    }
}
