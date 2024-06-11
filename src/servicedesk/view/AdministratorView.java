package servicedesk.view;

import servicedesk.StyledButton;
import servicedesk.models.Ticket;
import servicedesk.models.User;
import servicedesk.panels.BaseRolePanel;
import servicedesk.database.UserDatabase;
import servicedesk.database.TicketDatabase;
import servicedesk.controller.ApplicationController;
import servicedesk.enums.Role;
import servicedesk.enums.Category;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * View class for the Administrator Panel which is responsible for displaying
 * the administrator menu.
 */
public class AdministratorView extends BaseRolePanel {

    // Components
    private final ApplicationController appController;
    private JPanel contentPanel;
    private StyledButton viewAccountsButton;
    private StyledButton viewTicketsButton;
    private StyledButton logoutButton;
    private StyledButton editSpecialtiesButton;
    private StyledButton editRoleButton;
    private StyledButton editPasswordButton;

    // Constructor to initialize the AdministratorPanelView.
    public AdministratorView(JPanel mainPanel, CardLayout cardLayout, TicketDatabase ticketDatabase, UserDatabase userDatabase, ApplicationController appController) {
        super(ticketDatabase, userDatabase);
        this.appController = appController;

        // Set layout and background
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title label
        JLabel titleLabel = new JLabel("ADMINISTRATOR MENU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(new Color(0, 48, 63));
        add(titleLabel, BorderLayout.NORTH);

        // Create content wrapper panel
        JPanel contentWrapperPanel = createContentWrapperPanel();
        add(contentWrapperPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Create and return the content wrapper panel
    private JPanel createContentWrapperPanel() {
        JPanel contentWrapperPanel = new JPanel(new BorderLayout(10, 10));
        contentWrapperPanel.setBackground(Color.WHITE);
        contentWrapperPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 48, 63), 15));

        // Create content panel and add it to a scroll pane
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        contentWrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return contentWrapperPanel;
    }

    // Create and return the button panel
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialise buttons
        viewAccountsButton = new StyledButton("VIEW ACCOUNTS");
        viewTicketsButton = new StyledButton("VIEW TICKETS");
        logoutButton = new StyledButton("LOGOUT");

        // Add buttons to panel
        buttonPanel.add(viewAccountsButton);
        buttonPanel.add(viewTicketsButton);
        buttonPanel.add(logoutButton);

        return buttonPanel;
    }

    // Method to refresh the displayed accounts
    public void refreshAccounts(List<User> accounts) {
        contentPanel.removeAll();

        addSectionHeading(contentPanel, "Accounts");

        for (User account : accounts) {
            JPanel accountPanel = createAccountPanel(account);
            contentPanel.add(accountPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Create and return a panel for an account
    private JPanel createAccountPanel(User account) {
        JPanel accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create info panel
        JPanel infoPanel = createInfoPanel(account);
        accountPanel.add(infoPanel, BorderLayout.WEST);

        // Create button panel
        JPanel buttonPanel = createButtonPanel(account);
        accountPanel.add(buttonPanel, BorderLayout.EAST);

        return accountPanel;
    }

    // Create and return the info panel for an account
    private JPanel createInfoPanel(User account) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBackground(Color.WHITE);

        // Add account info labels
        JLabel nameLabel = new JLabel("Name: " + account.getFullName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel emailLabel = new JLabel("Email: " + account.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JLabel phoneLabel = new JLabel("Phone: " + account.getPhoneNumber());
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JLabel departmentLabel = new JLabel("Department: " + account.getDepartment());
        departmentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JLabel roleLabel = new JLabel("Role: " + account.getRole());
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Add labels to info panel
        infoPanel.add(nameLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(phoneLabel);
        infoPanel.add(departmentLabel);
        infoPanel.add(roleLabel);

        // If account is a technician then add the specialties label
        if (account.getRole() == Role.TECHNICIAN) {
            List<Category> specialties = userDatabase.getTechnicianSpecialities(account.getEmail());
            if (!specialties.isEmpty()) {
                StringBuilder specialtiesBuilder = new StringBuilder();
                for (int i = 0; i < specialties.size(); i++) {
                    specialtiesBuilder.append(specialties.get(i).toString());
                    if (i < specialties.size() - 1) {
                        specialtiesBuilder.append(", ");
                    }
                }
                JLabel specialtiesLabel = new JLabel("Specialties: " + specialtiesBuilder.toString());
                specialtiesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                infoPanel.add(specialtiesLabel);
            }
        }

        return infoPanel;
    }

    // Create and return the button panel for an account
    private JPanel createButtonPanel(User account) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Add edit role button
        editRoleButton = new StyledButton("Edit Role");
        editRoleButton.setPreferredSize(new Dimension(200, 30));
        editRoleButton.setFont(new Font("Arial", Font.BOLD, 14));
        editRoleButton.addActionListener(e -> appController.showEditUserPanel(account, "ROLE"));
        buttonPanel.add(editRoleButton, gbc);
        gbc.gridy++;

        // Add edit specialties button if account is a technician
        if (account.getRole() == Role.TECHNICIAN) {
            editSpecialtiesButton = new StyledButton("Edit Specialties");
            editSpecialtiesButton.setPreferredSize(new Dimension(200, 30));
            editSpecialtiesButton.setFont(new Font("Arial", Font.BOLD, 14));
            editSpecialtiesButton.addActionListener(e -> appController.showEditUserPanel(account, "SPECIALTIES"));
            buttonPanel.add(editSpecialtiesButton, gbc);
            gbc.gridy++;
        }

        // Add edit password button
        editPasswordButton = new StyledButton("Edit Password");
        editPasswordButton.setPreferredSize(new Dimension(200, 30));
        editPasswordButton.setFont(new Font("Arial", Font.BOLD, 14));
        editPasswordButton.addActionListener(e -> appController.showEditUserPanel(account, "PASSWORD"));
        buttonPanel.add(editPasswordButton, gbc);
        gbc.gridy++;

        return buttonPanel;
    }

    // Method to refresh the displayed tickets
    public void refreshTickets(List<Ticket> tickets) {
        contentPanel.removeAll();
        // Null passed instead of email as not required in this case. 
        addTicketsToPanel(contentPanel, tickets, ticketDatabase, Role.ADMINISTRATOR, null);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Getters for the buttons
    public StyledButton getViewAccountsButton() {
        return viewAccountsButton;
    }

    public StyledButton getViewTicketsButton() {
        return viewTicketsButton;
    }

    public StyledButton getLogoutButton() {
        return logoutButton;
    }
}
