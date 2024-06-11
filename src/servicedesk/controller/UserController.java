package servicedesk.controller;

import servicedesk.database.TicketDatabase;
import servicedesk.database.UserDatabase;
import servicedesk.models.User;
import servicedesk.view.UserView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class for the User Panel. Responsible for handling user
 * interactions through the buttons to create ticket, add comments and logout.
 */
public class UserController {

    // References to the view, databases, and application controller
    private final UserView view;
    private final ApplicationController appController;
    private final User user;

    // Default contructor to initialise the UserController 
    public UserController(UserView view, ApplicationController appController, User user) {
        this.view = view;
        this.appController = appController;
        this.user = user;

        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components
    private void initListeners() {
        // Action listener for the create ticket button
        view.getCreateTicketButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the create ticket panel
                appController.showCreateTicketPanel(user);
            }
        });

        // Action listener for the add comment button
        view.getAddCommentButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the add comment panel
                // appController.showAddCommentPanel(user, null);
            }
        });

        // Action listener for the logout button
        view.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the welcome panel
                appController.showWelcomePanel();
            }
        });
    }
}
