package servicedesk.panels;

import servicedesk.StyledButton;
import servicedesk.models.Comment;
import servicedesk.models.Ticket;
import servicedesk.database.TicketDatabase;
import servicedesk.database.UserDatabase;
import servicedesk.enums.Role;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class BaseRolePanel extends JPanel {

    // Fields for databases
    protected TicketDatabase ticketDatabase;
    protected UserDatabase userDatabase;

    // Constructor
    public BaseRolePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    // Setter methods for TicketDatabase and UserDatabase
    public void setTicketDatabase(TicketDatabase ticketDatabase) {
        this.ticketDatabase = ticketDatabase;
    }

    public void setUserDatabase(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    // Utility method to add a separator
    protected void addSeparator(JPanel panel) {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0, 48, 63));
        separator.setPreferredSize(new Dimension(separator.getPreferredSize().width, 10));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    // Utility method to format comment
    protected String formatComment(Comment comment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedTimestamp = comment.getTimestamp().format(formatter);
        return "[" + formattedTimestamp + "]  " + comment.getCreatedByEmail() + ": " + comment.getContent();
    }

    // Method to add a ticket component to the panel
    protected void addTicketComponent(JPanel panel, Ticket ticket, List<Comment> comments, Role role, String email) {
        JPanel ticketContainer = new JPanel(new BorderLayout());
        ticketContainer.setBackground(Color.WHITE);
        ticketContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left panel with ticket details
        JPanel leftPanel = createLeftPanel(ticket);
        ticketContainer.add(leftPanel, BorderLayout.WEST);

        // Right panel with ticket title, description and details
        JPanel rightPanel = createRightPanel(ticket, comments, role);
        ticketContainer.add(rightPanel, BorderLayout.CENTER);

        // Button panel for technicians
        if (role == Role.TECHNICIAN) {
            JPanel buttonPanel = createButtonPanel(ticket, email);
            ticketContainer.add(buttonPanel, BorderLayout.EAST);
        }

        // Add ticket container to the main panel
        panel.add(ticketContainer);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    // Method to create the left panel with ticket details
    private JPanel createLeftPanel(Ticket ticket) {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("ID: " + ticket.getTicketId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(idLabel);

        JLabel categoryLabel = new JLabel(ticket.getCategory().toString());
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        leftPanel.add(categoryLabel);

        JLabel dateLabel = new JLabel(ticket.getCreatedDate().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        leftPanel.add(dateLabel);

        leftPanel.setPreferredSize(new Dimension(80, leftPanel.getPreferredSize().height));
        return leftPanel;
    }

    // Method to create the right panel with ticket title, description, and details
    private JPanel createRightPanel(Ticket ticket, List<Comment> comments, Role role) {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JTextArea titleArea = createTitleArea(ticket.getTitle());
        rightPanel.add(titleArea, BorderLayout.NORTH);

        JTextArea descriptionArea = createDescriptionArea(ticket.getDescription());
        rightPanel.add(descriptionArea, BorderLayout.CENTER);

        JPanel detailsPanel = createDetailsPanel(ticket, comments, role);
        rightPanel.add(detailsPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    // Method to create the title area for the ticket
    private JTextArea createTitleArea(String title) {
        JTextArea titleArea = new JTextArea(title);
        titleArea.setLineWrap(true);
        titleArea.setWrapStyleWord(true);
        titleArea.setFont(new Font("Arial", Font.BOLD, 18));
        titleArea.setForeground(Color.BLACK);
        titleArea.setEditable(false);
        return titleArea;
    }

    // Method to create the description area for the ticket
    private JTextArea createDescriptionArea(String description) {
        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setEditable(false);
        return descriptionArea;
    }

    // Method to create the details panel with ticket details
    private JPanel createDetailsPanel(Ticket ticket, List<Comment> comments, Role role) {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);

        // Add the ticket details based on user role
        if (role == Role.TECHNICIAN || role == Role.ADMINISTRATOR) {
            addCreatedByDetails(ticket, detailsPanel);
        }
        if (role == Role.USER || role == Role.ADMINISTRATOR) {
            addAssignedToDetails(ticket, detailsPanel);
        }

        // Add comments if available
        addComments(comments, detailsPanel);

        return detailsPanel;
    }

    // Method to add created by details
    private void addCreatedByDetails(Ticket ticket, JPanel detailsPanel) {
        String createdByEmail = ticket.getCreatedByEmail();
        JLabel detailsTextLabel = new JLabel("Created By: " + createdByEmail);
        detailsTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsTextLabel.setForeground(Color.DARK_GRAY);
        detailsPanel.add(detailsTextLabel);
    }

    // Method to add assigned to details
    private void addAssignedToDetails(Ticket ticket, JPanel detailsPanel) {
        String technicianEmail = ticket.getAssignedTechnicianEmail();
        JLabel detailsTextLabel;
        if (technicianEmail != null) {
            // If someone is assigned to ticket
            detailsTextLabel = new JLabel("Assigned to: " + technicianEmail + " | Priority: " + ticket.getPriority());
        } else {
            // If no one is assgined to ticket
            detailsTextLabel = new JLabel("Not Assigned Yet | Priority: " + ticket.getPriority());
        }
        detailsTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsTextLabel.setForeground(Color.DARK_GRAY);
        detailsPanel.add(detailsTextLabel);
    }

    // Method to add comments
    private void addComments(List<Comment> comments, JPanel detailsPanel) {
        if (!comments.isEmpty()) {
            // If there are comments 
            JLabel commentsLabel = new JLabel("Comments:");
            commentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
            commentsLabel.setForeground(Color.BLACK);
            detailsPanel.add(commentsLabel);

            // Display each comment indivdually
            for (Comment comment : comments) {
                JLabel commentLabel = new JLabel(formatComment(comment));
                commentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                commentLabel.setForeground(Color.DARK_GRAY);
                detailsPanel.add(commentLabel);
            }
        }
    }

    // Method to create the button panel for technicians
    private JPanel createButtonPanel(Ticket ticket, String email) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 0, 5, 0);

        if (ticket.getAssignedTechnicianEmail() == null) {
            // Add claim button if there is no one assigned
            StyledButton claimTicketButton = createClaimTicketButton(ticket, email);
            buttonPanel.add(claimTicketButton, gbc);
        } else {
            // If someone is assigned then add change status button 
            StyledButton changeStatusButton = createChangeStatusButton(ticket, email);
            buttonPanel.add(changeStatusButton, gbc);
        }

        return buttonPanel;
    }

    // Method to create the claim ticket button
    private StyledButton createClaimTicketButton(Ticket ticket, String email) {
        StyledButton claimTicketButton = new StyledButton("CLAIM TICKET");
        claimTicketButton.setPreferredSize(new Dimension(130, 30));
        claimTicketButton.setFont(new Font("Arial", Font.BOLD, 12));
        claimTicketButton.addActionListener(e -> {
            ticket.setAssignedTechnicianEmail(email);
            ticketDatabase.updateTicket(ticket);
            removeAllAndRefresh(email);
        });
        return claimTicketButton;
    }

    // Method to create the change status button
    private StyledButton createChangeStatusButton(Ticket ticket, String email) {
        StyledButton changeStatusButton = new StyledButton("CHANGE STATUS");
        changeStatusButton.setPreferredSize(new Dimension(130, 30));
        changeStatusButton.setFont(new Font("Arial", Font.BOLD, 12));
        changeStatusButton.addActionListener(e -> {
            ticket.setResolved(!ticket.isResolved());
            ticketDatabase.updateTicket(ticket);
            removeAllAndRefresh(email);
        });
        return changeStatusButton;
    }

    // Method to remove all components from the panel and refresh the view
    private void removeAllAndRefresh(String email) {
        removeAll();
        List<Ticket> tickets = ticketDatabase.getTicketsForTechnician(email);
        addTicketsToPanel(this, tickets, ticketDatabase, Role.TECHNICIAN, email);
        revalidate();
        repaint();
    }

    // Method to add section heading
    protected void addSectionHeading(JPanel panel, String heading) {
        JPanel headingContainer = new JPanel(new BorderLayout());
        headingContainer.setBackground(Color.LIGHT_GRAY);
        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 48, 63));
        headingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headingContainer.add(headingLabel, BorderLayout.WEST);
        panel.add(headingContainer);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    // Method to add tickets to the panel
    protected void addTicketsToPanel(JPanel ticketPanel, List<Ticket> tickets, TicketDatabase database, Role role, String email) {
        boolean hasUnresolved = false;
        boolean hasResolved = false;

        for (Ticket ticket : tickets) {
            // If ticket is not resolved then display under this heading 
            if (!ticket.isResolved()) {
                if (!hasUnresolved) {
                    addSectionHeading(ticketPanel, "Unresolved Tickets");
                    hasUnresolved = true;
                }
                addTicketComponent(ticketPanel, ticket, database.getCommentsForTicket(ticket.getTicketId()), role, email);
            }
        }

        // Add a separator 
        if (hasUnresolved && tickets.stream().anyMatch(Ticket::isResolved)) {
            addSeparator(ticketPanel);
        }

        // If ticket is resolved then display under this heading 
        for (Ticket ticket : tickets) {
            if (ticket.isResolved()) {
                if (!hasResolved) {
                    addSectionHeading(ticketPanel, "Resolved Tickets");
                    hasResolved = true;
                }
                addTicketComponent(ticketPanel, ticket, database.getCommentsForTicket(ticket.getTicketId()), role, email);
            }
        }

        // If there are no tickets then display messsage
        if (!hasUnresolved && !hasResolved) {
            JLabel noTicketsLabel = new JLabel("No Tickets!");
            noTicketsLabel.setFont(new Font("Arial", Font.BOLD, 24));
            noTicketsLabel.setForeground(new Color(0, 48, 63));
            noTicketsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ticketPanel.add(noTicketsLabel);
        }
    }
}
