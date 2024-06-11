package servicedesk;

import servicedesk.database.*;
import servicedesk.controller.ApplicationController;

import javax.swing.*;
import java.awt.*;

public class Main {

    // Default colours for the GUI used across the panels. 
    // Located here to simplify the process of changing in the future. 
    public static final Color PRIMARY_COLOR = new Color(0, 48, 63);
    public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;

    public static void main(String[] args) {
        // Start the application on the event dispatch thread
        SwingUtilities.invokeLater(Main::startApplication);
    }

    private static void startApplication() {
        try {
            // Establish database connection
            DatabaseConnection dbConnection = new DatabaseConnection();

            // Create the main frame
            JFrame frame = createMainFrame();

            // Create the main panel for holding different views
            JPanel mainPanel = new JPanel();
            CardLayout cardLayout = new CardLayout();
            mainPanel.setLayout(cardLayout);

            // Add the main panel to the frame
            frame.add(mainPanel);
            frame.setVisible(true);

            UserDatabase userDatabase = new UserDatabase(dbConnection.getConnection());
            TicketDatabase ticketDatabase = new TicketDatabase(dbConnection.getConnection());

            // Main class that helps to navigate between panels.
            ApplicationController appController = new ApplicationController(userDatabase, ticketDatabase, mainPanel, cardLayout);

            // Show the welcome panel
            appController.showWelcomePanel();
        } catch (Exception ex) {
            // Display an error message if application fails to start up. 
            JOptionPane.showMessageDialog(null, "Failed to start the application: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Create the main frame of the application
    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Service Desk Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(950, 500));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
