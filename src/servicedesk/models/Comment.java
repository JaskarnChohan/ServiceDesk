package servicedesk.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {

    // Data fields to store all the comment's information.  
    private final int ticketId;
    private final LocalDateTime timestamp;
    private final String content;
    private final String createdByEmail;

    // Constructor to initialise comment object with provided data. 
    public Comment(int ticketId, LocalDateTime timestamp, String createdByEmail, String content) {
        this.ticketId = ticketId;
        this.timestamp = timestamp;
        this.createdByEmail = createdByEmail;
        this.content = content;
    }

    // Getters
    public int getTicketId() {
        return ticketId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    // Override toString method to display comments
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Format the timestamp using the formatter
        String formattedTimestamp = timestamp.format(formatter);

        // Construct the string representation of the comment
        return "[" + formattedTimestamp + "] " + createdByEmail + ": " + content;
    }
}
