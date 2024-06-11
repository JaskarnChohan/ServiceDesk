package servicedesk.controller;

import servicedesk.database.TicketDatabase;
import servicedesk.enums.Category;
import servicedesk.enums.Priority;
import servicedesk.models.User;
import servicedesk.view.CreateTicketView;
import servicedesk.CustomDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller class for the Create Ticket Panel. Responsible for handling user
 * interactions with the create ticket form.
 */
public class CreateTicketController {

    // References to the view, database, application controller, and authenticated user
    private final CreateTicketView view;
    private final TicketDatabase database;
    private final ApplicationController appController;
    private final User authenticatedUser;

    // Reference to the CreateTicketView, TicketDatabase, ApplicationController and User. 
    public CreateTicketController(CreateTicketView view, TicketDatabase database, ApplicationController appController, User user) {
        this.view = view;
        this.database = database;
        this.appController = appController;
        this.authenticatedUser = user;

        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components
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

    // Method to handle the submit button action. Validates the form fields and inserts a new ticket into the database. 
    private void handleSubmitButtonAction() {
        String title = view.getTitleField().getText().trim();
        String description = view.getDescriptionArea().getText().trim();
        Category category = (Category) view.getCategoryComboBox().getSelectedItem();
        Priority priority = (Priority) view.getPriorityComboBox().getSelectedItem();

        // Validate form fields to make sure they aren't null. 
        if (title.isEmpty() || description.isEmpty() || category == null || priority == null) {
            CustomDialog.showErrorMessage(view, "Please fill in all fields and make selections from the dropdowns");
            return;
        }

        // Prepare ticket data
        LocalDate createdDate = LocalDate.now();
        String userEmail = authenticatedUser.getEmail();
        boolean resolved = false;
        String assignedTechnicianEmail = null;

        // Insert ticket into the database
        try {
            database.insertTicket(title, description, category, priority, createdDate, userEmail, resolved, assignedTechnicianEmail);
            CustomDialog.showSuccessMessage(view, "Ticket submitted successfully!");
            view.clearFields();
            appController.showUserPanel(authenticatedUser);
        } catch (SQLException ex) {
            CustomDialog.showErrorMessage(view, "Failed to submit ticket: " + ex.getMessage());
        }
    }

    // Method to handle return button. Clears the fields and takes user back to user panel. 
    private void handleReturnButtonAction() {
        view.clearFields();
        appController.showUserPanel(authenticatedUser);
    }
}
