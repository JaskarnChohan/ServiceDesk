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

public class BaseRolePanel extends JPanel {

    protected final TicketDatabase ticketDatabase;
    protected final UserDatabase userDatabase;

    public BaseRolePanel(TicketDatabase ticketDatabase, UserDatabase userDatabase) {
        this.ticketDatabase = ticketDatabase;
        this.userDatabase = userDatabase;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    protected void addSeparator(JPanel panel) {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0, 48, 63));
        separator.setPreferredSize(new Dimension(separator.getPreferredSize().width, 10));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    protected String formatComment(Comment comment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedTimestamp = comment.getTimestamp().format(formatter);
        return "[" + formattedTimestamp + "]  " + comment.getCreatedByEmail() + ": " + comment.getContent();
    }

    protected void addTicketComponent(JPanel panel, Ticket ticket, List<Comment> comments, Role role, String email) {
        JPanel ticketContainer = new JPanel(new BorderLayout());
        ticketContainer.setBackground(Color.WHITE);
        ticketContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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
        ticketContainer.add(leftPanel, BorderLayout.WEST);

        ticketContainer.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.CENTER);
        ticketContainer.add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JTextArea titleArea = new JTextArea(ticket.getTitle());
        titleArea.setLineWrap(true);
        titleArea.setWrapStyleWord(true);
        titleArea.setFont(new Font("Arial", Font.BOLD, 18));
        titleArea.setForeground(Color.BLACK);
        titleArea.setEditable(false);
        rightPanel.add(titleArea, BorderLayout.NORTH);

        JTextArea descriptionArea = new JTextArea(ticket.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setEditable(false);
        rightPanel.add(descriptionArea, BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        detailsPanel.add(separator);

        if (role == Role.TECHNICIAN || role == Role.ADMINISTRATOR) {
            String createdByEmail = ticket.getCreatedByEmail();
            JLabel detailsTextLabel = new JLabel("Created By: " + createdByEmail);
            detailsTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            detailsTextLabel.setForeground(Color.DARK_GRAY);
            detailsPanel.add(detailsTextLabel);
        }
        if (role == Role.USER || role == Role.ADMINISTRATOR) {
            String technicianEmail = ticket.getAssignedTechnicianEmail();
            if (technicianEmail != null) {
                JLabel detailsTextLabel = new JLabel("Assigned to: " + technicianEmail + " | Priority: " + ticket.getPriority());
                detailsTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                detailsTextLabel.setForeground(Color.DARK_GRAY);
                detailsPanel.add(detailsTextLabel);
            } else {
                JLabel notAssignedLabel = new JLabel("Not Assigned Yet | Priority: " + ticket.getPriority());
                notAssignedLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                notAssignedLabel.setForeground(Color.DARK_GRAY);
                detailsPanel.add(notAssignedLabel);
            }
        }

        if (!comments.isEmpty()) {
            JLabel commentsLabel = new JLabel("Comments:");
            commentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
            commentsLabel.setForeground(Color.BLACK);
            detailsPanel.add(commentsLabel);

            for (Comment comment : comments) {
                JLabel commentLabel = new JLabel(formatComment(comment));
                commentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                commentLabel.setForeground(Color.DARK_GRAY);
                detailsPanel.add(commentLabel);
            }
        }

        rightPanel.add(detailsPanel, BorderLayout.SOUTH);

        ticketContainer.add(rightPanel, BorderLayout.CENTER);

        if (role == Role.TECHNICIAN) {
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
                StyledButton claimTicketButton = new StyledButton("CLAIM TICKET");
                claimTicketButton.setPreferredSize(new Dimension(130, 30));
                claimTicketButton.setFont(new Font("Arial", Font.BOLD, 12));
                claimTicketButton.addActionListener(e -> {
                    ticket.setAssignedTechnicianEmail(email);
                    ticketDatabase.updateTicket(ticket);
                    panel.removeAll();
                    List<Ticket> availableTickets = ticketDatabase.getAvailableTickets(userDatabase.getTechnicianSpecialities(email));
                    addTicketsToPanel(panel, availableTickets, ticketDatabase, Role.TECHNICIAN, email);
                    panel.revalidate();
                    panel.repaint();
                });
                buttonPanel.add(claimTicketButton, gbc);
            } else {
                StyledButton changeStatusButton = new StyledButton("CHANGE STATUS");
                changeStatusButton.setPreferredSize(new Dimension(130, 30));
                changeStatusButton.setFont(new Font("Arial", Font.BOLD, 12));
                changeStatusButton.addActionListener(e -> {
                    if (ticket.isResolved()) {
                        ticket.setResolved(false);
                    } else {
                        ticket.setResolved(true);
                    }

                    ticketDatabase.updateTicket(ticket);
                    panel.removeAll();
                    List<Ticket> tickets = ticketDatabase.getTicketsForTechnician(email);
                    addTicketsToPanel(panel, tickets, ticketDatabase, Role.TECHNICIAN, email);
                    panel.revalidate();
                    panel.repaint();
                });
                buttonPanel.add(changeStatusButton, gbc);

            }

            ticketContainer.add(buttonPanel, BorderLayout.EAST);
        }
        panel.add(ticketContainer);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

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

    protected void addTicketsToPanel(JPanel ticketPanel, List<Ticket> tickets, TicketDatabase database, Role role, String email) {
        boolean hasUnresolved = false;
        boolean hasResolved = false;

        for (Ticket ticket : tickets) {
            if (!ticket.isResolved()) {
                if (!hasUnresolved) {
                    addSectionHeading(ticketPanel, "Unresolved Tickets");
                    hasUnresolved = true;
                }
                addTicketComponent(ticketPanel, ticket, database.getCommentsForTicket(ticket.getTicketId()), role, email);
            }
        }

        if (hasUnresolved && tickets.stream().anyMatch(Ticket::isResolved)) {
            addSeparator(ticketPanel);
        }

        for (Ticket ticket : tickets) {
            if (ticket.isResolved()) {
                if (!hasResolved) {
                    addSectionHeading(ticketPanel, "Resolved Tickets");
                    hasResolved = true;
                }
                addTicketComponent(ticketPanel, ticket, database.getCommentsForTicket(ticket.getTicketId()), role, email);
            }
        }

        if (!hasUnresolved && !hasResolved) {
            JLabel noTicketsLabel = new JLabel("No Tickets!");
            noTicketsLabel.setFont(new Font("Arial", Font.BOLD, 24));
            noTicketsLabel.setForeground(new Color(0, 48, 63));
            noTicketsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ticketPanel.add(noTicketsLabel);
        }
    }

}
