package servicedesk.view;

import servicedesk.database.TicketDatabase;
import servicedesk.StyledButton;
import servicedesk.CustomDialog;
import servicedesk.models.Ticket;
import servicedesk.models.User;
import servicedesk.models.Technician;
import servicedesk.panels.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.sql.SQLException;

/**
 * View class for the Add Comment Panel which is responsible for displaying
 * the add comment form.
 */
public class AddCommentView extends BasePanel {

    // Components
    private JComboBox<Ticket> ticketComboBox;
    private JTextArea commentArea;
    private StyledButton submitButton;
    private StyledButton returnButton;
    
    // References to the database, authenticated user and technician. 
    private final TicketDatabase database;
    private final User authenticatedUser;
    private final Technician technician;

    // Constructor to initialize the AddCommentPanelView.
    public AddCommentView(JPanel mainPanel, CardLayout cardLayout, TicketDatabase database, User user, Technician technician) {
        super(mainPanel, cardLayout);
        this.database = database;
        this.authenticatedUser = user;
        this.technician = technician;

        // Create and add formButtonPanel
        JPanel formButtonPanel = createFormButtonPanel();

        // Create and add formPanel
        JPanel formPanel = createFormPanel();
        addTitle(formPanel, "Add Comment");

        // Add form fields to the formPanel
        addFormFields(formPanel);

        // Add form panel to the formButtonPanel
        formButtonPanel.add(formPanel, BorderLayout.CENTER);

        // Create and add button panel
        JPanel buttonPanel = createButtonPanel();
        formButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add form button panel to the main panel
        add(formButtonPanel, BorderLayout.CENTER);
    }

    // Method to add form fields to the given formPanel. 
    private void addFormFields(JPanel formPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Ticket dropdown
        ticketComboBox = new JComboBox<>();
        ticketComboBox.setSelectedIndex(-1);
        refreshTicketComboBox();
        addField(formPanel, "Ticket", ticketComboBox, gbc);

        // Comment area
        gbc.gridy++;
        commentArea = new JTextArea(5, 20);
        addDescriptionField(formPanel, "Comment", commentArea, gbc);
    }

    // Method to refresh the ticket combo box with tickets revelant to the user or technician. 
    private void refreshTicketComboBox() {
        try {
            ticketComboBox.removeAllItems();
            List<Ticket> tickets;
            if (authenticatedUser != null) {
                tickets = database.getUserOpenTickets(authenticatedUser);
            } else {
                tickets = database.getTicketsForTechnician(technician.getEmail());
            }
            for (Ticket ticket : tickets) {
                ticketComboBox.addItem(ticket);
            }
            ticketComboBox.setSelectedIndex(-1);
        } catch (SQLException e) {
            CustomDialog.showErrorMessage(this, "Failed to load tickets: " + e.getMessage());
        }
    }

    // Method to create and return the button panel. 
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));

        returnButton = new StyledButton("RETURN");
        returnButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(returnButton);

        submitButton = new StyledButton("SUBMIT");
        submitButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(submitButton);

        return buttonPanel;
    }

    // Getters for the components. Used by the controller.
    public Ticket getSelectedTicket() {
        return (Ticket) ticketComboBox.getSelectedItem();
    }

    public String getComment() {
        return commentArea.getText().trim();
    }

    public StyledButton getSubmitButton() {
        return submitButton;
    }

    public StyledButton getReturnButton() {
        return returnButton;
    }

    // Method to clear all fields in the form. Called from controller.
    public void clearFields() {
        ticketComboBox.setSelectedIndex(-1);
        commentArea.setText("");
    }
}
