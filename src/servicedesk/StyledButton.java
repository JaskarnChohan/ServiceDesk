package servicedesk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {

    // Constants for the styling of the button itself
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 25);
    private static final Insets BUTTON_MARGIN = new Insets(20, 20, 10, 20);
    private static final Color DEFAULT_BACKGROUND_COLOR = Main.PRIMARY_COLOR; // Use primary colour from Main class
    private static final Color HOVER_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    private static final Color HOVER_FOREGROUND_COLOR = Color.BLACK;
    private static final int BORDER_THICKNESS = 5;

    // Default constructor to make a JButton into this styled button 
    public StyledButton(String text) {
        super(text);
        styleButton();
    }

    // Method to style the button
    private void styleButton() {
        setPreferredSize(new Dimension(160, 45)); // Set preferred size
        setFont(BUTTON_FONT); // Set font
        setMargin(BUTTON_MARGIN); // Set margin
        setFocusPainted(false); // Disable focus painting
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor to hand
        setBackground(DEFAULT_BACKGROUND_COLOR); // Set default background color
        setForeground(DEFAULT_FOREGROUND_COLOR); // Set default foreground color
        setBorder(BorderFactory.createEmptyBorder()); // Set empty border

        // Add mouse listeners to create hover effects to make it easier for the user
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_BACKGROUND_COLOR); // Change background on hover
                setForeground(HOVER_FOREGROUND_COLOR); // Change foreground on hover
                setBorder(BorderFactory.createLineBorder(HOVER_FOREGROUND_COLOR, BORDER_THICKNESS)); // Add border on hover
            }

            // Once user is no longer hovering then change back. 
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(DEFAULT_BACKGROUND_COLOR); // Change back to default background
                setForeground(DEFAULT_FOREGROUND_COLOR); // Change back to  default foreground 
                setBorder(BorderFactory.createEmptyBorder()); // Remove border 
            }
        });
    }

    // Override paintComponent method to customise the painting behaviour
    @Override
    protected void paintComponent(Graphics g) {
        // Fill the background with the button's background color
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    // Override setBackground method to ensure consistency in behaviour
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }
}
