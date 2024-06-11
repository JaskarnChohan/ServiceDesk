package servicedesk;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for displaying custom dialog messages.
 */
public class CustomDialog {

    // Image icons for success and error messages
    private static final Icon SUCCESS_ICON = new ImageIcon(CustomDialog.class.getClassLoader().getResource("images/success.png"));
    private static final Icon ERROR_ICON = new ImageIcon(CustomDialog.class.getClassLoader().getResource("images/error.png"));

    // Method to display a success message dialog
    public static void showSuccessMessage(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Success", JOptionPane.INFORMATION_MESSAGE, SUCCESS_ICON);
    }

    // Method to display a error message dialog
    public static void showErrorMessage(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Error", JOptionPane.ERROR_MESSAGE, ERROR_ICON);
    }

}
