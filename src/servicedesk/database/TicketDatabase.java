package servicedesk.database;

import servicedesk.enums.Category;
import servicedesk.enums.Priority;
import servicedesk.models.Comment;
import servicedesk.models.Ticket;
import servicedesk.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Method to store all database methods revelant to the tickets/comments. 
public class TicketDatabase {

    private final Connection conn;

    // Default contructor to get the connection 
    public TicketDatabase(Connection conn) {
        this.conn = conn;
    }

    // Get user tickets using the user's email.
    public List<Ticket> getUserTickets(String userEmail) {
        String sql = "SELECT * FROM tickets WHERE created_by_user_email = ?";
        return getTickets(sql, userEmail);
    }

    // Get comments for a ticket using the ticketId.
    public List<Comment> getCommentsForTicket(int ticketId) {
        List<Comment> comments = new ArrayList<>();
        String commentQuery = "SELECT * FROM comments WHERE ticket_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(commentQuery)) {
            pstmt.setInt(1, ticketId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                    String createdByEmail = rs.getString("created_by_email");
                    String content = rs.getString("content");
                    comments.add(new Comment(ticketId, timestamp, createdByEmail, content));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    // Insert a new ticket into the database.
    public void insertTicket(String title, String description, Category category, Priority priority, LocalDate createdDate, String createdByUserEmail, boolean resolved, String assignedTechnicianEmail) throws SQLException {
        int nextId = getNextTicketId();

        if (nextId == -1) {
            System.err.println("Failed to retrieve next ticket ID.");
            return;
        }

        String sql = "INSERT INTO tickets (ticket_id, category, title, description, priority, created_date, created_by_user_email, assigned_technician_email, resolved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nextId);
            pstmt.setString(2, category.name());
            pstmt.setString(3, title);
            pstmt.setString(4, description);
            pstmt.setString(5, priority.name());
            pstmt.setTimestamp(6, Timestamp.valueOf(createdDate.atStartOfDay()));
            pstmt.setString(7, createdByUserEmail);
            pstmt.setString(8, assignedTechnicianEmail);
            pstmt.setBoolean(9, resolved);
            pstmt.executeUpdate();
        }
    }

    // Get the next ticket ID.
    private int getNextTicketId() {
        String query = "SELECT MAX(ticket_id) AS max_id FROM tickets";

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Get a user's open tickets.
    public List<Ticket> getUserOpenTickets(User user) {
        String sql = "SELECT * FROM tickets WHERE created_by_user_email = ? AND resolved = false";
        return getTickets(sql, user.getEmail());
    }

    // Get tickets assigned to a technician.
    public List<Ticket> getTicketsForTechnician(String technicianEmail) {
        String sql = "SELECT * FROM tickets WHERE assigned_technician_email = ?";
        return getTickets(sql, technicianEmail);
    }

    // Insert a comment into the database.
    public void insertComment(int ticketId, LocalDateTime timestamp, String createdByEmail, String content) throws SQLException {
        String sql = "INSERT INTO comments (ticket_id, timestamp, created_by_email, content) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            pstmt.setTimestamp(2, Timestamp.valueOf(timestamp));
            pstmt.setString(3, createdByEmail);
            pstmt.setString(4, content);
            pstmt.executeUpdate();
        }
    }

    // Get all tickets in the database.
    public List<Ticket> getAllTickets() {
        String sql = "SELECT * FROM tickets";
        return getTickets(sql);
    }

    // Get available tickets based on technician's specialties.
    public List<Ticket> getAvailableTickets(List<Category> technicianSpecialties) {
        List<Ticket> availableTickets = new ArrayList<>();

        if (technicianSpecialties.isEmpty()) {
            return availableTickets;
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tickets WHERE assigned_technician_email IS NULL AND (");

        for (int i = 0; i < technicianSpecialties.size(); i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("category = '").append(technicianSpecialties.get(i)).append("'");
        }

        queryBuilder.append(")");

        return getTickets(queryBuilder.toString());
    }

    // Update a ticket in the database.
    public boolean updateTicket(Ticket ticket) {
        String sql = "UPDATE tickets SET category = ?, title = ?, description = ?, priority = ?, created_date = ?, created_by_user_email = ?, assigned_technician_email = ?, resolved = ? WHERE ticket_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getCategory().name());
            pstmt.setString(2, ticket.getTitle());
            pstmt.setString(3, ticket.getDescription());
            pstmt.setString(4, ticket.getPriority().name());
            pstmt.setTimestamp(5, Timestamp.valueOf(ticket.getCreatedDate().atStartOfDay()));
            pstmt.setString(6, ticket.getCreatedByEmail());
            pstmt.setString(7, ticket.getAssignedTechnicianEmail());
            pstmt.setBoolean(8, ticket.isResolved());
            pstmt.setInt(9, ticket.getTicketId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to get tickets based on a query and a parameter.
    private List<Ticket> getTickets(String sql, String... params) {
        List<Ticket> tickets = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, params[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRowToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    // Helper method to map a row from the ResultSet to the Ticket object.
    private Ticket mapRowToTicket(ResultSet rs) throws SQLException {
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

        return new Ticket(ticketId, category, title, description, priority, createdDate, createdByUserEmail, assignedTechnicianEmail, resolved, comments);
    }
}
