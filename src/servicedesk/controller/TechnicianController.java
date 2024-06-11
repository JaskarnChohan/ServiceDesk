package servicedesk.controller;

import servicedesk.database.TicketDatabase;
import servicedesk.models.Technician;
import servicedesk.models.Ticket;
import servicedesk.view.TechnicianView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller class for the Technician Panel responsible for handling user interactions and updating the view accordingly.
 */
public class TechnicianController {

    // References to the view, database, application controller, and technician.
    private final TechnicianView view;
    private final ApplicationController appController;
    private final TicketDatabase database;
    private final Technician technician;

    // Constructor to initialize the TechnicianPanelController.
    public TechnicianController(TechnicianView view, TicketDatabase database, ApplicationController appController, Technician technician) {
        this.view = view;
        this.database = database;
        this.appController = appController;
        this.technician = technician;

        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components
    private void initListeners() {
        // Add action listeners to buttons
        view.getViewTicketsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTickets();
            }
        });

        view.getAddCommentButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Take sure to add comment panel. 
                appController.showAddCommentPanel(null, technician);
            }
        });

        view.getViewAvailableTicketsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAvailableTickets();
            }
        });

        view.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Take user back to welcome panel. 
                appController.showWelcomePanel();
            }
        });
    }

    // Method to refresh the displayed tickets
    private void refreshTickets() {
        List<Ticket> tickets = database.getTicketsForTechnician(technician.getEmail());
        view.refreshTickets(tickets);
    }

    // Method to refresh the displayed available tickets
    private void refreshAvailableTickets() {
        List<Ticket> availableTickets = database.getAvailableTickets(technician.getSpecialties());
        view.refreshAvailableTickets(availableTickets);
    }

}
