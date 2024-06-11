package servicedesk.models;

import java.time.LocalDateTime;

public class Comment {

    // Data fields to store all the comment's information.  
    private final int commentId;
    private final int ticketId;
    private final LocalDateTime timestamp;
    private final String content;
    private final String createdByEmail;

    // Constructor to initialise comment object with provided data. 
    public Comment(int commentId, int ticketId, LocalDateTime timestamp, String createdByEmail, String content) {
        this.commentId = commentId;
        this.ticketId = ticketId;
        this.timestamp = timestamp;
        this.createdByEmail = createdByEmail;
        this.content = content;
    }

    // Getters
    public int getCommentId() {
        return commentId;
    }

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
        return "[" + timestamp + "] " + createdByEmail + ": " + content;
    }
}
