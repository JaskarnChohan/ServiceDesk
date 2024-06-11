package servicedesk.controller;

import servicedesk.database.UserDatabase;
import servicedesk.PasswordHasher;
import servicedesk.models.User;
import servicedesk.models.Technician;
import servicedesk.enums.Role;
import servicedesk.enums.Category;
import servicedesk.view.LoginView;

import servicedesk.CustomDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller class for the Login Panel. Responsible for handling user
 * interactions through the buttons to login.
 */
public class LoginController {

    // Reference to the LoginView, Database, and ApplicationController
    private final LoginView view;
    private final UserDatabase database;
    private final ApplicationController appController;

    // Reference to the LoginView, Database and ApplicationController
    public LoginController(LoginView view, UserDatabase database, ApplicationController appController) {
        this.view = view;
        this.database = database;
        this.appController = appController;
        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components
    private void initListeners() {
        // Action listener for the login button
        view.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = view.getEmail().toLowerCase().trim();
                String password = view.getPassword();

                // Hash the user's entered password to match the hashed password in the database. 
                PasswordHasher passwordHasher = new PasswordHasher();
                String hashedPassword = passwordHasher.hashPassword(password);

                // Validate user input to see both fields are filled.
                if (email.isEmpty() || password.isEmpty()) {
                    CustomDialog.showErrorMessage(view, "Please fill in all fields");
                    return;
                }

                // Authenticate user using database method. 
                User authenticatedUser = database.authenticateUser(email, hashedPassword);

                // If authenticatedUser is not null then proceed to the role menu. 
                if (authenticatedUser != null) {
                    CustomDialog.showSuccessMessage(view, "Login successful!");
                    handleUserRole(authenticatedUser);
                    view.clearFields(); // Clear the fields 
                } else {
                    // Display error message for incorrect login details 
                    CustomDialog.showErrorMessage(view, "Invalid email or password");
                }
            }
        });

        // Action listener for the return button
        view.getReturnButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.clearFields(); // Clear the fields. 
                appController.showWelcomePanel(); // Go back to welcome page. 
            }
        });
    }

    // Method to display different menus depending on user's role after successful authentication 
    private void handleUserRole(User authenticatedUser) {
        Role userRole = authenticatedUser.getRole();
        switch (userRole) {
            case USER:
                appController.showUserPanel(authenticatedUser);
                break;
            case ADMINISTRATOR:
                appController.showAdministratorPanel();
                break;
            case TECHNICIAN:
                // Create a new technician object. 
                Technician technician = createTechnician(authenticatedUser.getEmail());
                appController.showTechnicianPanel(technician);
                break;
            default:
                // Highly unlikely but if incorrect role in database. 
                CustomDialog.showErrorMessage(view, "Unknown user role");
        }
    }

    // Method to create a technician object based on the email. 
    private Technician createTechnician(String email) {
        List<Category> specialties = database.getTechnicianSpecialties(email);
        User user = database.getUserByEmail(email);
        if (user != null) {
            return new Technician(user.getFullName(), user.getEmail(), user.getHashedPassword(), user.getPhoneNumber(), user.getDepartment(), specialties);
        } else {
            CustomDialog.showErrorMessage(view, "Failed to load technician.");
            return null;
        }
    }
}
