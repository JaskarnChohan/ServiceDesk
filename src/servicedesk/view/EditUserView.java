package servicedesk.view;

import servicedesk.database.UserDatabase;
import servicedesk.StyledButton;
import servicedesk.enums.Category;
import servicedesk.enums.Role;
import servicedesk.models.User;
import servicedesk.panels.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// View class for editing user details such as role, specialties, and password. 
public class EditUserView extends BasePanel {

    // Components
    private JComboBox<Role> roleComboBox;
    private JPanel specialtiesPanel;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private final JButton submitButton;
    private final JButton returnButton;
    private final User user;
    private final UserDatabase database; 

    // Constructor to initialise the EditUserPanelView.
    public EditUserView(JPanel mainPanel, CardLayout cardLayout, UserDatabase database, User user, String editType) {
        super(mainPanel, cardLayout);
        this.database = database; 
        this.user = user;

        JPanel formButtonPanel = createFormButtonPanel();
        JPanel formPanel = createFormPanel();

        String title = "";
        // Check the edit type that the administrator is doing. 
        if (editType.equals("ROLE")) {
            title = "Edit Role";
            addRoleFields(formPanel);
        } else if (editType.equals("SPECIALTIES")) {
            title = "Edit Specialties";
            addSpecialtiesFields(formPanel);
        } else if (editType.equals("PASSWORD")) {
            title = "Change Password";
            addPasswordField(formPanel);
        }

        addTitle(formPanel, title);
        formButtonPanel.add(formPanel, BorderLayout.CENTER);

        submitButton = new StyledButton("SUBMIT");
        returnButton = new StyledButton("RETURN");

        JPanel buttonPanel = createButtonPanel(returnButton, submitButton);
        formButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(formButtonPanel, BorderLayout.CENTER);
    }

    // Method to add role fields to the form panel.
    private void addRoleFields(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;

        addField(panel, "New Role", roleComboBox = new JComboBox<>(Role.values()), gbc);
        roleComboBox.setSelectedIndex(-1);
    }

    // Method to add specialties fields to the form panel.
    private void addSpecialtiesFields(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Add current specialties label
        JLabel currentSpecialtiesLabel = new JLabel("Current Specialties: ");
        currentSpecialtiesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(currentSpecialtiesLabel, gbc);

        // Add current specialties value
        List<Category> currentSpecialties = database.getTechnicianSpecialties(user.getEmail());
        List<String> currentSpecialtyNames = new ArrayList<>();
        for (Category specialty : currentSpecialties) {
            currentSpecialtyNames.add(specialty.toString());
        }
        JLabel currentSpecialtiesValueLabel = new JLabel(String.join(", ", currentSpecialtyNames));
        currentSpecialtiesValueLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx++;
        panel.add(currentSpecialtiesValueLabel, gbc);

        // Add new specialties checkboxes
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel newSpecialtiesLabel = new JLabel("New Specialties:");
        newSpecialtiesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(newSpecialtiesLabel, gbc);

        gbc.gridx++;
        specialtiesPanel = new JPanel(new GridLayout(0, 1));
        specialtiesPanel.setBackground(Color.LIGHT_GRAY);
        for (Category category : Category.values()) {
            JCheckBox checkBox = new JCheckBox(category.toString());
            checkBox.setFont(new Font("Arial", Font.PLAIN, 16));
            specialtiesPanel.add(checkBox);
        }

        JScrollPane specialtiesScrollPane = new JScrollPane(specialtiesPanel);
        specialtiesScrollPane.setPreferredSize(new Dimension(150, 100));
        panel.add(specialtiesScrollPane, gbc);
    }

    // Method to add password fields to the form panel.
    private void addPasswordField(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;

        addField(panel, "New Password", passwordField = new JPasswordField(), gbc);
        gbc.gridy++;
        addField(panel, "Confirm Password", confirmPasswordField = new JPasswordField(), gbc);
    }

    // Getters for the components.
    public JComboBox<Role> getRoleComboBox() {
        return roleComboBox;
    }

    public JPanel getSpecialtiesPanel() {
        return specialtiesPanel;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getReturnButton() {
        return returnButton;
    }
}
