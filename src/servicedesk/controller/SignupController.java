package servicedesk.controller;

import servicedesk.database.UserDatabase;
import servicedesk.enums.Role;
import servicedesk.models.User;
import servicedesk.view.SignupView;
import servicedesk.PasswordHasher;
import servicedesk.CustomDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class for the Signup Panel. Responsible for handling user
 * interactions through the buttons to signup. 
 */
public class SignupController {

    // Reference to the SignupView, Database and ApplicationController
    private final SignupView view;
    private final UserDatabase database;
    private final ApplicationController appController;

    // Default contructor to initialise the SignupController 
    public SignupController(SignupView view, UserDatabase database, ApplicationController appController) {
        this.view = view;
        this.database = database;
        this.appController = appController;
        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components
    private void initListeners() {
        // Action listener for the signup button
        view.getSignupButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve user input from form fields
                String firstName = view.getFirstNameField().getText();
                String lastName = view.getLastNameField().getText();
                String email = view.getEmailField().getText().toLowerCase();
                String password = new String(view.getPasswordField().getPassword());
                String phoneNumber = view.getPhoneNumberField().getText();
                String department = view.getDepartmentField().getText();

                // Validate the input fields from user to prevent errors. 
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || department.isEmpty()) {
                    CustomDialog.showErrorMessage(view, "Please fill in all fields!");
                    return;
                }

                // Check if email is valid by checking if it contains an '@'. 
                if (!email.contains("@")) {
                    CustomDialog.showErrorMessage(view, "Invalid email address format!");
                    return;
                }

                // Check database to see if user exists. 
                if (database.userExists(email)) {
                    CustomDialog.showErrorMessage(view, "User with this email already exists!");
                    return;
                }

                // Hash the password before storing to protect the user's password 
                PasswordHasher passwordHasher = new PasswordHasher();
                String hashedPassword = passwordHasher.hashPassword(password);

                // Create new user object
                // Default role is just 'USER' 
                User user = new User(firstName + " " + lastName, email, hashedPassword, phoneNumber, department, Role.USER);

                // Save user to the database once everything is checked. 
                if (database.saveUser(user)) {
                    CustomDialog.showSuccessMessage(view, "Signup successful!");
                    view.clearFields(); // Clear the form fields
                    appController.showLoginPanel(); // Switch to login panel for the user to login. 
                } else {
                    CustomDialog.showErrorMessage(view, "Signup failed. Please try again.");
                }
            }
        });

        // Action listener for the return button
        view.getReturnButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.clearFields(); // Clear the form fields
                appController.showWelcomePanel(); // Switch back to the welcome panel
            }
        });
    }
}
