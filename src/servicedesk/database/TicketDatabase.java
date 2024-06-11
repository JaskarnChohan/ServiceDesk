package servicedesk.database;

import servicedesk.enums.Category;
import servicedesk.enums.Priority;
import servicedesk.models.Comment;
import servicedesk.models.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketDatabase {

    private Connection conn;

    public TicketDatabase(Connection conn) {
        this.conn = conn;
    }

    // Method to get user tickets using the user's email. 
    public List<Ticket> getUserTickets(String userEmail) {
        List<Ticket> userTickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE created_by_user_email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int ticketId = rs.getInt("ticket_id");
                    Category category = Category.valueOf(rs.getString("category"));
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    Priority priority = Priority.valueOf(rs.getString("priority"));
                    LocalDate createdDate = rs.getTimestamp("created_date").toLocalDateTime().toLocalDate();
                    String createdByUserEmail = rs.getString("created_by_user_email");
                    String assignedTechnicianEmail = rs.getString("assigned_technician_email");
                    boolean resolved = rs.getBoolean("resolved");

                    List<Comment> comments = getCommentsForTicket(ticketId);

                    Ticket ticket = new Ticket(ticketId, category, title, description, priority, createdDate, createdByUserEmail, assignedTechnicianEmail, resolved, comments);
                    userTickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userTickets;
    }

    // Private method to get comments for a ticket using the ticketId. 
    private List<Comment> getCommentsForTicket(int ticketId) {
        List<Comment> comments = new ArrayList<>();
        String commentQuery = "SELECT * FROM comments WHERE ticket_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(commentQuery)) {
            pstmt.setInt(1, ticketId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int commentId = rs.getInt("comment_id");
                    java.time.LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                    String createdByEmail = rs.getString("created_by_email");
                    String content = rs.getString("content");

                    Comment comment = new Comment(commentId, ticketId, timestamp, createdByEmail, content);
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}
