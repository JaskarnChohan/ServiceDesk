package servicedesk.view;

import servicedesk.StyledButton;
import servicedesk.models.Ticket;
import servicedesk.models.User;
import servicedesk.panels.BaseRolePanel;
import servicedesk.database.UserDatabase;
import servicedesk.database.TicketDatabase;
import servicedesk.enums.Role;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * View class for the User Panel which is responsible for displaying the user
 * menu.
 */
public class UserView extends BaseRolePanel {

    // Components
    private JButton createTicketButton;
    private JButton addCommentButton;
    private JButton logoutButton;
    private JPanel ticketContentPanel;
    private final TicketDatabase ticketDatabase;
    private final UserDatabase userDatabase;
    private final User user;

    /**
     * Constructor to initialize the UserPanelView.
     *
     * @param mainPanel The main panel where this view will be added.
     * @param cardLayout The card layout to switch between panels.
     * @param ticketDatabase The ticket database instance to get any possible
     * ticket data.
     * @param userDatabase The user database instance to get user data.
     * @param user The user instance representing the current logged in user.
     */
    public UserView(JPanel mainPanel, CardLayout cardLayout, TicketDatabase ticketDatabase, UserDatabase userDatabase, User user) {
        this.ticketDatabase = ticketDatabase;
        this.userDatabase = userDatabase;
        this.user = user;

        // Create and add title label
        addTitleLabel();

        // Create and add ticket panel
        JPanel ticketPanel = createTicketPanel();
        add(ticketPanel, BorderLayout.CENTER);

        // Create and add button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Load and display user tickets
        refreshTickets();
    }

    // Add the title label to the panel. 
    private void addTitleLabel() {
        JLabel titleLabel = new JLabel("USER MENU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(new Color(0, 48, 63));
        add(titleLabel, BorderLayout.NORTH);
    }

    // Create and return the ticket panel 
    private JPanel createTicketPanel() {
        JPanel ticketPanel = new JPanel(new BorderLayout(10, 10));
        ticketPanel.setBackground(Color.WHITE);
        ticketPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 48, 63), 15));

        // Create ticket content panel and add it to a scroll pane
        ticketContentPanel = new JPanel();
        ticketContentPanel.setLayout(new BoxLayout(ticketContentPanel, BoxLayout.Y_AXIS));
        ticketContentPanel.setBackground(Color.LIGHT_GRAY);
        ticketContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(ticketContentPanel);
        ticketPanel.add(scrollPane, BorderLayout.CENTER);

        return ticketPanel;
    }

    // Method to create and return the button panel. 
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createTicketButton = new StyledButton("CREATE TICKET");
        buttonPanel.add(createTicketButton);

        addCommentButton = new StyledButton("ADD COMMENT");
        buttonPanel.add(addCommentButton);

        logoutButton = new StyledButton("LOGOUT");
        buttonPanel.add(logoutButton);

        return buttonPanel;
    }

    // Getters for the components. Used by the controller.
    public JButton getCreateTicketButton() {
        return createTicketButton;
    }

    public JButton getAddCommentButton() {
        return addCommentButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    /**
     * Method to refresh the tickets displayed in the ticket content panel. This
     * method is called from the controller.
     */
    public void refreshTickets() {
        ticketContentPanel.removeAll();
        List<Ticket> tickets = ticketDatabase.getUserTickets(user.getEmail());
        addTicketsToPanel(ticketContentPanel, tickets, ticketDatabase, Role.USER, user.getEmail());
        ticketContentPanel.revalidate();
        ticketContentPanel.repaint();
    }
}
