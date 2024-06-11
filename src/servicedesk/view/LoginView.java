package servicedesk.view;

import servicedesk.StyledButton;
import servicedesk.panels.BasePanel;

import javax.swing.*;
import java.awt.*;

/**
 * View class for the Login Panel which is responsible for displaying the login
 * page.
 */
public class LoginView extends BasePanel {

    // Components
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton returnButton;

    /**
     * Constructor to initialise the LoginPanelView.
     *
     * @param mainPanel The main panel where this view will be added.
     * @param cardLayout The card layout to switch between panels.
     */
    public LoginView(JPanel mainPanel, CardLayout cardLayout) {
        super(mainPanel, cardLayout);

        // Create form panel and button panel
        JPanel formButtonPanel = createFormButtonPanel();
        JPanel formPanel = createFormPanel();

        // Add title to the form panel
        addTitle(formPanel, "Login");

        // Add form fields to the form panel and improve design with gbc
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        addField(formPanel, "Email", emailField = new JTextField(20), gbc);
        addField(formPanel, "Password", passwordField = new JPasswordField(20), gbc);

        // Add form panel to the formButton panel
        formButtonPanel.add(formPanel, BorderLayout.CENTER);

        // Create and add buttons to the button panel
        loginButton = new StyledButton("LOGIN");
        returnButton = new StyledButton("RETURN");
        JPanel buttonPanel = createButtonPanel(returnButton, loginButton);
        formButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add formButtonPanel to the main panel
        add(formButtonPanel, BorderLayout.CENTER);
    }

    // Getters for the components. Used by controller
    public JTextField getEmailField() {
        return emailField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getReturnButton() {
        return returnButton;
    }

    // Method to get email input from the email field
    public String getEmail() {
        return emailField.getText().toLowerCase();
    }

    // Method to get password input from the password field
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Method to clear all fields which is called from controller
    public void clearFields() {
        clearFields(emailField, passwordField);
    }
}
