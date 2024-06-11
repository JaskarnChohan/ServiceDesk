package servicedesk.controller;

import servicedesk.CustomDialog;
import servicedesk.database.TicketDatabase;
import servicedesk.models.Ticket;
import servicedesk.models.User;
import servicedesk.models.Technician;
import servicedesk.view.AddCommentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Controller class for the Add Comment Panel. Responsible for handling user
 * interactions with the add comment form.
 */
public class AddCommentController {

    // References to the view, database, application controller, and authenticated user
    private AddCommentView view;
    private final TicketDatabase database;
    private final ApplicationController appController;
    private final User authenticatedUser;
    private final Technician technician;

    // Reference to the AddCommentView, TicketDatabase, ApplicationController, User and technician.
    // NOTE: Either user or technician will be left null depending on the user's role. 
    public AddCommentController(AddCommentView view, TicketDatabase database, ApplicationController appController, User user, Technician technician) {
        this.view = view;
        this.database = database;
        this.appController = appController;
        this.authenticatedUser = user;
        this.technician = technician;

        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components.
    private void initListeners() {
        // Action listener for the submit button
        view.getSubmitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmitButtonAction();
            }
        });

        // Action listener for the return button
        view.getReturnButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReturnButtonAction();
            }
        });
    }

    // Method to handle the submit button action. 
    private void handleSubmitButtonAction() {
        Ticket selectedTicket = view.getSelectedTicket();
        String comment = view.getComment();

        // Perform validation to check if nothing is left null. 
        if (selectedTicket == null || comment.isEmpty()) {
            CustomDialog.showErrorMessage(view, "Please select a ticket and enter a comment.");
            return;
        }

        LocalDateTime timestamp = LocalDateTime.now();

        // Insert comment into database
        try {
            if (authenticatedUser != null) {
                String userEmail = authenticatedUser.getEmail();
                database.insertComment(selectedTicket.getTicketId(), timestamp, userEmail, comment);
                appController.showUserPanel(authenticatedUser);
            } else {
                String technicianEmail = technician.getEmail();
                database.insertComment(selectedTicket.getTicketId(), timestamp, technicianEmail, comment);
                // appController.showTechnicianPanel(technician);
            }
            CustomDialog.showSuccessMessage(view, "Comment added successfully!");
            view.clearFields();
        } catch (SQLException ex) {
            CustomDialog.showErrorMessage(view, "Failed to add comment: " + ex.getMessage());
        }
    }

    // Method to handle the return button action depending on if its a user or technician. 
    private void handleReturnButtonAction() {
        view.clearFields();
        if (authenticatedUser != null) {
            appController.showUserPanel(authenticatedUser);
        } else {
            // appController.showTechnicianPanel(technician);
        }
    }
}
