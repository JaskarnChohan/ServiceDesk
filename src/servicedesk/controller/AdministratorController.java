package servicedesk.controller;

import servicedesk.view.AdministratorView;
import servicedesk.database.UserDatabase;
import servicedesk.database.TicketDatabase;
import servicedesk.models.User;
import servicedesk.models.Ticket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller class for the Administrator Panel. Handles user interactions and
 * updates the view accordingly.
 */
public class AdministratorController {

    // References to the view, database, and application controller
    private final AdministratorView view;
    private final TicketDatabase ticketDatabase;
    private final UserDatabase userDatabase;
    private final ApplicationController appController;

    // Constructor to initialise the AdministratorPanelController.
    public AdministratorController(AdministratorView view, TicketDatabase ticketDatabase, UserDatabase userDatabase, ApplicationController appController) {
        this.view = view;
        this.ticketDatabase = ticketDatabase;
        this.userDatabase = userDatabase;
        this.appController = appController;

        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components
    private void initListeners() {
        // Action listener for the view accounts button
        view.getViewAccountsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve and refresh accounts from the database
                List<User> accounts = userDatabase.getAllUsers();
                view.refreshAccounts(accounts);
            }
        });

        // Action listener for the view tickets button
        view.getViewTicketsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve and refresh tickets from the database
                List<Ticket> tickets = ticketDatabase.getAllTickets();
                view.refreshTickets(tickets);
            }
        });

        // Action listener for the logout button
        view.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the welcome panel when logout button is clicked
                appController.showWelcomePanel();
            }
        });
    }
}
