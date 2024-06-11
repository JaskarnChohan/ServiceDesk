package servicedesk.controller;

import servicedesk.view.WelcomeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeController {

    // Reference to the SignupPanelView and ApplicationController
    private final WelcomeView view;
    private final ApplicationController appController;

    // Default contructor to initialise the WelcomeController 
    public WelcomeController(WelcomeView view, ApplicationController appController) {
        this.view = view;
        this.appController = appController;
        // Call method to set up event listenrns 
        setupListeners();
    }

    // Method to set up event listeners for the view components
    private void setupListeners() {
        // Add action listener for the login button
        view.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make the application controller switch to login panel 
                appController.showLoginPanel();
            }
        });

        // Add action listener for the signup button
        view.getSignupButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make the application controller switch to signup panel
                appController.showSignupPanel();
            }
        });
    }
}
