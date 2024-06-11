package servicedesk.database;

import servicedesk.enums.Category;
import servicedesk.enums.Priority;
import servicedesk.models.Comment;
import servicedesk.models.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import servicedesk.models.User;

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

    // Method to get comments for a ticket using the ticketId. 
    public List<Comment> getCommentsForTicket(int ticketId) {
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

    // Method to insert a new ticket into the table. 
    public void insertTicket(String title, String description, Category category, Priority priority, LocalDate createdDate, String createdByUserEmail, boolean resolved, String assignedTechnicianEmail) throws SQLException {
        int nextId = getNextTicketId();
        if (nextId == -1) {
            System.err.println("Failed to retrieve next ticket ID.");
            return;
        }
        String insertTicketSQL = "INSERT INTO tickets (ticket_id, category, title, description, priority, created_date, created_by_user_email, assigned_technician_email, resolved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertTicketSQL)) {
            pstmt.setInt(1, nextId);
            pstmt.setString(2, category.name());
            pstmt.setString(3, title);
            pstmt.setString(4, description);
            pstmt.setString(5, priority.name());
            pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(createdDate.atStartOfDay()));
            pstmt.setString(7, createdByUserEmail);
            pstmt.setString(8, assignedTechnicianEmail);
            pstmt.setBoolean(9, resolved);
            pstmt.executeUpdate();
        }
    }

    // Private method to return the next ticket id.  
    private int getNextTicketId() {
        String query = "SELECT MAX(ticket_id) AS max_id FROM tickets";
        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return maxId + 1;
            } else {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Method to get a user's tickets that are currently not resolved. 
    public List<Ticket> getUserOpenTickets(User user) throws SQLException {
        List<Ticket> userTickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE created_by_user_email = ? AND resolved = false";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getEmail());
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userTickets;
    }

    // Method to get tickets for a technician that are assigned to them. 
    public List<Ticket> getTicketsForTechnician(String technicianEmail) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE assigned_technician_email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, technicianEmail);
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
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    // Method to insert a comment into the table connected to a ticket. 
    public void insertComment(int ticketId, LocalDateTime timestamp, String createdByEmail, String content) throws SQLException {
        String query = "INSERT INTO comments (ticket_id, timestamp, created_by_email, content) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, ticketId);
            statement.setTimestamp(2, Timestamp.valueOf(timestamp));
            statement.setString(3, createdByEmail);
            statement.setString(4, content);
            statement.executeUpdate();
        }
    }

    // Method to get all tickets in the table. 
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets";
        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
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
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
}
