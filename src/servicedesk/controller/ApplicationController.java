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

    public void showWelcomePanel() {
        WelcomeView welcomeView = new WelcomeView(mainPanel, cardLayout);
        mainPanel.add(welcomeView, "WelcomePanel");
        WelcomeController welcomeController = new WelcomeController(welcomeView, this);
        cardLayout.show(mainPanel, "WelcomePanel");
    }

    public void showSignupPanel() {
        SignupView signupView = new SignupView(mainPanel, cardLayout);
        mainPanel.add(signupView, "SignupPanel");
        SignupController signupPanelController = new SignupController(signupView, userDatabase, this);
        cardLayout.show(mainPanel, "SignupPanel");
    }

    public void showLoginPanel() {
        LoginView loginView = new LoginView(mainPanel, cardLayout);
        mainPanel.add(loginView, "LoginPanel");
        LoginController loginPanelController = new LoginController(loginView, userDatabase, this);
        cardLayout.show(mainPanel, "LoginPanel");
    }

    public void showUserPanel(User authenticatedUser) {
        UserView userView = new UserView(mainPanel, cardLayout, ticketDatabase, userDatabase, authenticatedUser);
        mainPanel.add(userView, "UserPanel");
        UserController userPanelController = new UserController(userView, this, authenticatedUser);
        cardLayout.show(mainPanel, "UserPanel");
        userView.refreshTickets();
    }

    public void showCreateTicketPanel(User authenticatedUser) {
        CreateTicketView createTicketView = new CreateTicketView(mainPanel, cardLayout);
        mainPanel.add(createTicketView, "CreateTicketPanel");
        CreateTicketController createTicketController = new CreateTicketController(createTicketView, ticketDatabase, this, authenticatedUser);
        cardLayout.show(mainPanel, "CreateTicketPanel");
    }

    public void showAddCommentPanel(User authenticatedUser, Technician technician) {
        AddCommentView addCommentView = new AddCommentView(mainPanel, cardLayout, ticketDatabase, authenticatedUser, technician);
        mainPanel.add(addCommentView, "AddCommentPanel");
        AddCommentController createTicketController = new AddCommentController(addCommentView, ticketDatabase, this, authenticatedUser, technician);
        cardLayout.show(mainPanel, "AddCommentPanel");
    }

    public void showAdministratorPanel() {
        AdministratorView administratorView = new AdministratorView(mainPanel, cardLayout, ticketDatabase, userDatabase, this);
        mainPanel.add(administratorView, "AdministratorPanel");
        AdministratorController administratorController = new AdministratorController(administratorView, ticketDatabase, userDatabase, this);
        cardLayout.show(mainPanel, "AdministratorPanel");
    }

    public void showEditUserPanel(User user, String editType) {
        EditUserView editUserView = new EditUserView(mainPanel, cardLayout, userDatabase, user, editType);
        mainPanel.add(editUserView, "EditUserPanel");
        EditUserController editUserController = new EditUserController(editUserView, userDatabase, this, user, editType);
        cardLayout.show(mainPanel, "EditUserPanel");
    }
}
