package servicedesk.view;

import servicedesk.StyledButton;
import servicedesk.panels.BasePanel;

import javax.swing.*;
import java.awt.*;

/**
 * View class for the Signup Panel which is responsible for displaying the
 * signup page.
 */
public class SignupView extends BasePanel {

    // Components
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JTextField phoneNumberField;
    private final JTextField departmentField;
    private final JButton signupButton;
    private final JButton returnButton;

    /**
     * Constructor to initialise the SignupView.
     *
     * @param mainPanel The main panel where this view will be added.
     * @param cardLayout The card layout to switch between panels.
     */
    public SignupView(JPanel mainPanel, CardLayout cardLayout) {
        super(mainPanel, cardLayout);

        // Create form panel and button panel
        JPanel formButtonPanel = createFormButtonPanel();
        JPanel formPanel = createFormPanel();

        // Add title to the form panel
        addTitle(formPanel, "Sign Up");

        // Add form fields to the form panel and improve design with gbc
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        addField(formPanel, "First Name", firstNameField = new JTextField(20), gbc);
        addField(formPanel, "Last Name", lastNameField = new JTextField(20), gbc);
        addField(formPanel, "Email", emailField = new JTextField(20), gbc);
        addField(formPanel, "Password", passwordField = new JPasswordField(20), gbc);
        addField(formPanel, "Phone Number", phoneNumberField = new JTextField(20), gbc);
        addField(formPanel, "Department", departmentField = new JTextField(20), gbc);

        // Add form panel to the formButton panel
        formButtonPanel.add(formPanel, BorderLayout.CENTER);

        // Create and add buttons to the button panel
        signupButton = new StyledButton("SIGN UP");
        returnButton = new StyledButton("RETURN");
        JPanel buttonPanel = createButtonPanel(returnButton, signupButton);
        formButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add formButtonPanel to the main panel
        add(formButtonPanel, BorderLayout.CENTER);
    }

    // Getters for the components. Used by controller
    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextField getPhoneNumberField() {
        return phoneNumberField;
    }

    public JTextField getDepartmentField() {
        return departmentField;
    }

    public JButton getSignupButton() {
        return signupButton;
    }

    public JButton getReturnButton() {
        return returnButton;
    }

    // Method to clear all fields which is called from controller. 
    public void clearFields() {
        clearFields(firstNameField, lastNameField, emailField, passwordField, phoneNumberField, departmentField);
    }
}
