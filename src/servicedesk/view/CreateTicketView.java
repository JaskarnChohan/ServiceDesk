package servicedesk.view;

import servicedesk.enums.Category;
import servicedesk.enums.Priority;
import servicedesk.StyledButton;
import servicedesk.panels.BasePanel;

import javax.swing.*;
import java.awt.*;

/**
 * View class for the Create Ticket Panel which is responsible for displaying
 * the create ticket form.
 */
public class CreateTicketView extends BasePanel {

    // Components
    private JComboBox<Category> categoryComboBox;
    private JComboBox<Priority> priorityComboBox;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private StyledButton submitButton;
    private StyledButton returnButton;

    /**
     * Constructor to initialize the CreateTicketView.
     *
     * @param mainPanel The main panel where this view will be added.
     * @param cardLayout The card layout to switch between panels.
     */
    public CreateTicketView(JPanel mainPanel, CardLayout cardLayout) {
        super(mainPanel, cardLayout);

        // Create and add formButtonPanel
        JPanel formButtonPanel = createFormButtonPanel();

        // Create and add formPanel
        JPanel formPanel = createFormPanel();
        addTitle(formPanel, "Create Ticket");

        // Add form fields to the formPanel
        addFormFields(formPanel);

        // Add form panel to the formButtonPanel
        formButtonPanel.add(formPanel, BorderLayout.CENTER);

        // Create and add button panel
        JPanel buttonPanel = createButtonPanel();
        formButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add form button panel to the main panel
        add(formButtonPanel, BorderLayout.CENTER);
    }

    // Method to add form fields to given panel. 
    private void addFormFields(JPanel formPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;

        addField(formPanel, "Title", titleField = new JTextField(20), gbc);

        gbc.gridy++;
        addDescriptionField(formPanel, "Description", descriptionArea = new JTextArea(5, 20), gbc);

        gbc.gridy++;
        addField(formPanel, "Category", categoryComboBox = new JComboBox<>(Category.values()), gbc);
        categoryComboBox.setSelectedIndex(-1);

        gbc.gridy++;
        addField(formPanel, "Priority", priorityComboBox = new JComboBox<>(Priority.values()), gbc);
        priorityComboBox.setSelectedIndex(-1);
    }

    // Method to create and return the button panel. 
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));

        returnButton = new StyledButton("RETURN");
        returnButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(returnButton);

        submitButton = new StyledButton("SUBMIT");
        submitButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(submitButton);

        return buttonPanel;
    }

    // Getters for the components. Used by the controller.
    public JComboBox<Category> getCategoryComboBox() {
        return categoryComboBox;
    }

    public JComboBox<Priority> getPriorityComboBox() {
        return priorityComboBox;
    }

    public JTextField getTitleField() {
        return titleField;
    }

    public JTextArea getDescriptionArea() {
        return descriptionArea;
    }

    public StyledButton getSubmitButton() {
        return submitButton;
    }

    public StyledButton getReturnButton() {
        return returnButton;
    }

    // Method to clear all fields in the form. Called from controller. 
    public void clearFields() {
        titleField.setText("");
        descriptionArea.setText("");
        categoryComboBox.setSelectedIndex(-1);
        priorityComboBox.setSelectedIndex(-1);
    }
}
