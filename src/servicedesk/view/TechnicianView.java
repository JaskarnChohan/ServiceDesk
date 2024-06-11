package servicedesk.view;

import servicedesk.StyledButton;
import servicedesk.models.Ticket;
import servicedesk.models.Technician;
import servicedesk.panels.BaseRolePanel;
import servicedesk.enums.Role;
import servicedesk.database.TicketDatabase;
import servicedesk.database.UserDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * View class for the Technician Panel responsible for displaying the technician
 * menu.
 */
public class TechnicianView extends BaseRolePanel {

    // Components
    private JPanel contentPanel;
    private JButton viewTicketsButton;
    private JButton viewAvailableTicketsButton;
    private JButton logoutButton;
    private JButton addCommentButton;
    private final TicketDatabase ticketDatabase;
    private final UserDatabase userDatabase;
    private final Technician technician;

    // Constructor to initialize the TechnicianPanelView.
    public TechnicianView(JPanel mainPanel, CardLayout cardLayout, TicketDatabase ticketDatabase, UserDatabase userDatabase, Technician technician) {
        this.ticketDatabase = ticketDatabase;
        this.userDatabase = userDatabase;
        this.technician = technician;

        addTitleLabel();
        addContentWrapperPanel();
        addButtonPanel();
    }

    // Method to add the title label
    private void addTitleLabel() {
        JLabel titleLabel = new JLabel("TECHNICIAN MENU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(new Color(0, 48, 63));
        add(titleLabel, BorderLayout.NORTH);
    }

    // Method to add the content wrapper panel
    private void addContentWrapperPanel() {
        JPanel contentWrapperPanel = new JPanel(new BorderLayout(10, 10));
        contentWrapperPanel.setBackground(Color.WHITE);
        contentWrapperPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 48, 63), 15));

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        contentWrapperPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel initialMessageLabel = new JLabel("Please select an option below.");
        initialMessageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        initialMessageLabel.setForeground(new Color(0, 48, 63));
        initialMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(initialMessageLabel);

        add(contentWrapperPanel, BorderLayout.CENTER);
    }

    // Method to add the button panel
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the buttons 
        viewTicketsButton = new StyledButton("MY TICKETS");
        viewAvailableTicketsButton = new StyledButton("AVAILABLE");
        addCommentButton = new StyledButton("ADD COMMENT");
        logoutButton = new StyledButton("LOGOUT");

        // Add the buttons 
        buttonPanel.add(viewTicketsButton);
        buttonPanel.add(viewAvailableTicketsButton);
        buttonPanel.add(addCommentButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to refresh the displayed tickets that are already assigned
    public void refreshTickets(List<Ticket> tickets) {
        contentPanel.removeAll();
        addTicketsToPanel(contentPanel, tickets, ticketDatabase, Role.TECHNICIAN, technician.getEmail());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Method to refresh the displayed available tickets
    public void refreshAvailableTickets(List<Ticket> availableTickets) {
        contentPanel.removeAll();
        addTicketsToPanel(contentPanel, availableTickets, ticketDatabase, Role.TECHNICIAN, technician.getEmail());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Getters for the buttons
    public JButton getViewTicketsButton() {
        return viewTicketsButton;
    }

    public JButton getViewAvailableTicketsButton() {
        return viewAvailableTicketsButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public JButton getAddCommentButton() {
        return addCommentButton;
    }

    public void setDatabases(TicketDatabase ticketDatabase, UserDatabase userDatabase) {
        setTicketDatabase(ticketDatabase);
        setUserDatabase(userDatabase);
    }
}
