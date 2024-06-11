package servicedesk.controller;

import servicedesk.database.TicketDatabase;
import servicedesk.database.UserDatabase;
import servicedesk.view.*;
import servicedesk.models.User;
import servicedesk.models.Technician;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class ApplicationController {

    private final UserDatabase userDatabase;
    private final TicketDatabase ticketDatabase;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    public ApplicationController(UserDatabase userDatabase, TicketDatabase ticketDatabase, JPanel mainPanel, CardLayout cardLayout) {
        this.userDatabase = userDatabase;
        this.ticketDatabase = ticketDatabase;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
    }

    // Show the welcome panel
    public void showWelcomePanel() {
        WelcomeView welcomeView = new WelcomeView(mainPanel, cardLayout);
        WelcomeController welcomeController = new WelcomeController(welcomeView, this);
        addViewAndShow(welcomeView, "WelcomePanel");
    }

    // Show the signup panel
    public void showSignupPanel() {
        SignupView signupView = new SignupView(mainPanel, cardLayout);
        SignupController signupController = new SignupController(signupView, userDatabase, this);
        addViewAndShow(signupView, "SignupPanel");
    }

    // Show the login panel
    public void showLoginPanel() {
        LoginView loginView = new LoginView(mainPanel, cardLayout);
        LoginController loginController = new LoginController(loginView, userDatabase, this);
        addViewAndShow(loginView, "LoginPanel");
    }

    // Show the user panel
    public void showUserPanel(User authenticatedUser) {
        UserView userView = new UserView(mainPanel, cardLayout, ticketDatabase, userDatabase, authenticatedUser);
        UserController userController = new UserController(userView, this, authenticatedUser);
        addViewAndShow(userView, "UserPanel");
        userView.refreshTickets();
    }

    // Show the create ticket panel
    public void showCreateTicketPanel(User authenticatedUser) {
        CreateTicketView createTicketView = new CreateTicketView(mainPanel, cardLayout);
        CreateTicketController createTicketController = new CreateTicketController(createTicketView, ticketDatabase, this, authenticatedUser);
        addViewAndShow(createTicketView, "CreateTicketPanel");
    }

    // Show the add comment panel
    public void showAddCommentPanel(User authenticatedUser, Technician technician) {
        AddCommentView addCommentView = new AddCommentView(mainPanel, cardLayout, ticketDatabase, authenticatedUser, technician);
        AddCommentController addCommentController = new AddCommentController(addCommentView, ticketDatabase, this, authenticatedUser, technician);
        addViewAndShow(addCommentView, "AddCommentPanel");
    }

    // Show the administrator panel
    public void showAdministratorPanel() {
        AdministratorView administratorView = new AdministratorView(mainPanel, cardLayout, ticketDatabase, userDatabase, this);
        AdministratorController administratorController = new AdministratorController(administratorView, ticketDatabase, userDatabase, this);
        addViewAndShow(administratorView, "AdministratorPanel");
    }

    // Show the edit user panel
    public void showEditUserPanel(User user, String editType) {
        EditUserView editUserView = new EditUserView(mainPanel, cardLayout, userDatabase, user, editType);
        EditUserController editUserController = new EditUserController(editUserView, userDatabase, this, user, editType);
        addViewAndShow(editUserView, "EditUserPanel");
    }

    // Show the technician panel
    public void showTechnicianPanel(Technician technician) {
        TechnicianView technicianView = new TechnicianView(mainPanel, cardLayout, ticketDatabase, userDatabase, technician);
        technicianView.setDatabases(ticketDatabase, userDatabase);  // Ensure databases are set correctly
        TechnicianController technicianController = new TechnicianController(technicianView, ticketDatabase, this, technician);
        addViewAndShow(technicianView, "TechnicianPanel");
    }

    // Helper method to add a view to the main panel and show it
    private void addViewAndShow(JPanel view, String panelName) {
        mainPanel.add(view, panelName);
        cardLayout.show(mainPanel, panelName);
    }
}
