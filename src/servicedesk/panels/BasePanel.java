package servicedesk.panels;

import servicedesk.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class BasePanel extends JPanel {

    protected JPanel mainPanel;
    protected CardLayout cardLayout;
    private Color primaryColor = Main.PRIMARY_COLOR;
    private Color backgroundColor = Main.BACKGROUND_COLOR;

    public BasePanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());
        ImageIcon sidePanelIcon = new ImageIcon(getClass().getClassLoader().getResource("images/sidepanel.jpg"));
        Image sidePanelImage = sidePanelIcon.getImage();
        loadSidePanelImage(sidePanelImage);
    }

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

    protected JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        return formPanel;
    }
    
    protected JPanel createFormButtonPanel() {
            JPanel formButtonPanel = new JPanel();
        formButtonPanel.setLayout(new BorderLayout(20, 20));
        formButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return formButtonPanel;
    }
        

        

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

    protected void addMessage(JPanel panel, String message, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy++;
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(messageLabel, gbc);
    }

    protected void addField(JPanel panel, String labelText, JComponent component, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 30, 0, 20);
        panel.add(label, gbc);
        gbc.gridx++;
        component.setPreferredSize(new Dimension(100, 40));
        component.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 5, 0, 20);
        panel.add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
    }

        protected void addDescriptionField(JPanel panel, String labelText, JTextArea textArea, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 30, 10, 20);
        panel.add(label, gbc);
        gbc.gridx++;
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 5, 10, 20);
        panel.add(scrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
    }
        
    protected void addLabel(JPanel panel, String labelText, GridBagConstraints gbc) {
    JLabel label = new JLabel(labelText + ":");
    label.setFont(new Font("Arial", Font.PLAIN, 18));
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(10, 30, 0, 20);
    panel.add(label, gbc);
}

protected void addTextField(JPanel panel, JTextField textField, GridBagConstraints gbc) {
    textField.setPreferredSize(new Dimension(100, 40));
    textField.setFont(new Font("Arial", Font.PLAIN, 18));
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(10, 5, 0, 20);
    panel.add(textField, gbc);
}


    protected void addButton(JPanel panel, JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        panel.add(button);
    }

    protected JPanel createButtonPanel(JButton... buttons) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, buttons.length, 10, 10));
        for (JButton button : buttons) {
            addButton(buttonPanel, button);
        }
        return buttonPanel;
    }

    protected void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public void initListeners(JButton button, ActionListener listener) {
        button.addActionListener(listener);
    }
}
