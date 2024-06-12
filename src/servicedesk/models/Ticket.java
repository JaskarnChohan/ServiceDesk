package servicedesk.models;

import servicedesk.enums.Category;
import servicedesk.enums.Priority;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private final int ticketId;
    private final Category category;
    private String title;
    private String description;
    private final Priority priority;
    private final LocalDate createdDate;
    private final String createdByUserEmail;
    private boolean resolved;
    private List<Comment> comments = new ArrayList<>();
    private String assignedTechnicianEmail;

    // Constructor to create a new Ticket object
    public Ticket(int ticketId, Category category, String title, String description, Priority priority, LocalDate createdDate, String createdByUserEmail, String assignedTechnicianEmail, boolean resolved, List<Comment> comments) {
        this.ticketId = ticketId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.createdDate = createdDate;
        this.createdByUserEmail = createdByUserEmail;
        this.assignedTechnicianEmail = assignedTechnicianEmail;
        this.resolved = resolved;
        this.comments = comments;
    }

    // Getters
    public int getTicketId() {
        return ticketId;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getCreatedByEmail() {
        return createdByUserEmail;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    // To change the resolved status of a ticket
    public void changeResolvedStatus(boolean newStatus) {
        this.resolved = newStatus;
    }

    // To add a comment to a ticket
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    // To get a list of comments
    public List<Comment> getComments() {
        return comments;
    }

    // Get the assigned technician's email
    public String getAssignedTechnicianEmail() {
        return assignedTechnicianEmail;
    }

    // Set the assigned technician's email
    public void setAssignedTechnicianEmail(String assignedTechnicianEmail) {
        this.assignedTechnicianEmail = assignedTechnicianEmail;
    }

    // Set description of ticket
    public void setDescription(String description) {
        this.description = description;
    }

    // toString() for drop down boxes. 
    @Override
    public String toString() {
        return "Ticket ID: " + ticketId + " - " + title;
    }

}
