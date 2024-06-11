package servicedesk.panels;

import servicedesk.Main;

import javax.swing.*;
import java.awt.*;

// Abstract class that other panels use to get the design elements to ensure all pages look the same. 
public abstract class BasePanel extends JPanel {

    protected JPanel mainPanel;
    protected CardLayout cardLayout;
    private final Color primaryColor = Main.PRIMARY_COLOR;
    private final Color backgroundColor = Main.BACKGROUND_COLOR;

    // Default constructor which creates the body layout, gets the image and loads it up.
    public BasePanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());
        ImageIcon sidePanelIcon = new ImageIcon(getClass().getClassLoader().getResource("images/sidepanel.jpg"));
        Image sidePanelImage = sidePanelIcon.getImage();
        loadSidePanelImage(sidePanelImage);
    }

    // Method to load side image panel used across the panel. 
    protected void loadSidePanelImage(Image sidePanelImage) {
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(sidePanelImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setPreferredSize(new Dimension(400, 0));
        add(imagePanel, BorderLayout.WEST);
    }

    // Method to create the default form panel.
    protected JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        return formPanel;
    }

    // Method to create a default form button panel.
    protected JPanel createFormButtonPanel() {
        JPanel formButtonPanel = new JPanel();
        formButtonPanel.setLayout(new BorderLayout(20, 20));
        formButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return formButtonPanel;
    }

    // Message to add title onto panel.
    protected void addTitle(JPanel panel, String title) {
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(primaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(titleLabel, gbc);
    }

    // Method to add message onto panel 
    protected void addMessage(JPanel panel, String message, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy++;
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(messageLabel, gbc);
    }

    // Method to add a normal field onto the panel 
    protected void addField(JPanel panel, String labelText, JComponent component, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        setComponentConstraints(gbc, 10, 30, 0, 20, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, 1.0);
        panel.add(label, gbc);
        gbc.gridx++;
        component.setPreferredSize(new Dimension(100, 40));
        component.setFont(new Font("Arial", Font.PLAIN, 18));
        setComponentConstraints(gbc, 5, 0, 0, 20, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0);
        panel.add(component, gbc);
        resetConstraints(gbc);
    }

    // Method to add a description field 
    protected void addDescriptionField(JPanel panel, String labelText, JTextArea textArea, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        setComponentConstraints(gbc, 10, 30, 0, 20, GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, 1.0);
        panel.add(label, gbc);
        gbc.gridx++;
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        setComponentConstraints(gbc, 5, 0, 0, 20, GridBagConstraints.WEST, GridBagConstraints.BOTH, 1.0);
        panel.add(scrollPane, gbc);
        resetConstraints(gbc);
    }

    // Method to add labels onto panel 
    protected void addLabel(JPanel panel, String labelText, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        setComponentConstraints(gbc, 10, 30, 0, 20, GridBagConstraints.EAST, GridBagConstraints.NONE, 0.0);
        panel.add(label, gbc);
    }

    // Method to add a text field 
    protected void addTextField(JPanel panel, JTextField textField, GridBagConstraints gbc) {
        textField.setPreferredSize(new Dimension(100, 40));
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        setComponentConstraints(gbc, 5, 0, 0, 20, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0);
        panel.add(textField, gbc);
    }

    // Method to add button onto panel 
    protected void addButton(JPanel panel, JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        panel.add(button);
    }

    // Method to create default button panel 
    protected JPanel createButtonPanel(JButton... buttons) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, buttons.length, 10, 10));
        for (JButton button : buttons) {
            addButton(buttonPanel, button);
        }
        return buttonPanel;
    }

    // Method to clear all fields. 
    protected void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    // Helper methods for design 
    private void setComponentConstraints(GridBagConstraints gbc, int top, int left, int bottom, int right, int anchor, int fill, double weightx) {
        gbc.insets = new Insets(top, left, bottom, right);
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = weightx;
    }

    private void resetConstraints(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
    }
}
