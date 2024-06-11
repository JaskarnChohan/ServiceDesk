package servicedesk.view;

import javax.swing.*;
import java.awt.*;
import servicedesk.StyledButton;
import servicedesk.panels.BasePanel;

public class WelcomeView extends BasePanel {

    // Components
    private final StyledButton loginButton;
    private final StyledButton signupButton;

    // Default constructor
    public WelcomeView(JPanel mainPanel, CardLayout cardLayout) {
        super(mainPanel, cardLayout);

        // Panel to hold welcome message and buttons
        JPanel messageButtonPanel = new JPanel();
        messageButtonPanel.setLayout(new BorderLayout(20, 20)); // Set layout with spacing
        messageButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Added padding

        // Panel for welcome message
        JPanel messagePanel = createFormPanel(); // Create panel with form layout
        addTitle(messagePanel, "Welcome to Service Desk"); // Add title
        GridBagConstraints gbc = new GridBagConstraints(); // Create constraints for better message placement
        gbc.gridx = 0;
        gbc.gridy = 1;
        addMessage(messagePanel, "Please click on one of the buttons below to proceed:", gbc); // Add message

        // Add the message panel to messageButtonPanel itself
        messageButtonPanel.add(messagePanel, BorderLayout.CENTER);

        // Create a login and signup button
        loginButton = new StyledButton("LOGIN");
        signupButton = new StyledButton("SIGN UP");

        // Panel for buttons
        JPanel buttonPanel = createButtonPanel(loginButton, signupButton);

        // Add the button panel to parent messageButtonPanel
        messageButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add messageButtonPanel to WelcomePanelView
        add(messageButtonPanel, BorderLayout.CENTER);
    }

    // Getter for login button
    public StyledButton getLoginButton() {
        return loginButton;
    }

    // Getter for signup button
    public StyledButton getSignupButton() {
        return signupButton;
    }
}
