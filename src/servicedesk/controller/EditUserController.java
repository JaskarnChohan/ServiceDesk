package servicedesk.controller;

import servicedesk.database.UserDatabase;
import servicedesk.PasswordHasher;
import servicedesk.enums.Category;
import servicedesk.enums.Role;
import servicedesk.models.User;
import servicedesk.view.EditUserView;
import servicedesk.CustomDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/// Controller class for editing user details.
 
public class EditUserController {

    // References to the user database, view, user being edited, edit type, and application controller
    private final UserDatabase database;
    private final EditUserView view;
    private final User user;
    private final String editType;
    private final ApplicationController appController;

    // Constructor for EditUserController.
    public EditUserController(EditUserView view, UserDatabase database, ApplicationController appController, User user, String editType) {
        this.view = view;
        this.database = database;
        this.user = user;
        this.editType = editType;
        this.appController = appController;

        // Call method to set up event listeners
        initListeners();
    }

    // Method to set up event listeners for the view components
    private void initListeners() {
        view.getSubmitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmitAction();
            }
        });

        view.getReturnButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Take administrator back to administrator panel. 
                appController.showAdministratorPanel();
            }
        });
    }

    // Handles the submit action based on the edit type. 
    private void handleSubmitAction() {
        switch (editType) {
            case "ROLE":
                handleRoleUpdate();
                break;
            case "SPECIALTIES":
                handleSpecialtiesUpdate();
                break;
            case "PASSWORD":
                handlePasswordUpdate();
                break;
        }
        appController.showAdministratorPanel();
    }


    // Method that handles updating the user's role 
    private void handleRoleUpdate() {
        Role selectedRole = (Role) view.getRoleComboBox().getSelectedItem();
        // Checks if administrator has picked a role. 
        if (selectedRole == null) {
            CustomDialog.showErrorMessage(view, "Please select a role");
            return;
        }
        // Update the user's role. 
        updateUserRole(selectedRole);
    }

    // Method that updates a user's specialties. 
    private void handleSpecialtiesUpdate() {
        List<Category> selectedSpecialties = getSelectedSpecialties();
        // Checks if at least one speciality has been chosen. 
        if (selectedSpecialties.isEmpty()) {
            CustomDialog.showErrorMessage(view, "Please select at least one specialty");
            return;
        }
        // Updates the specialities. 
        updateUserSpecialties(selectedSpecialties);
    }

    // Method that gets the selected specialties from the view and returns a list. 
    private List<Category> getSelectedSpecialties() {
        List<Category> selectedSpecialties = new ArrayList<>();
        for (Component component : view.getSpecialtiesPanel().getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    selectedSpecialties.add(Category.valueOf(checkBox.getText()));
                }
            }
        }
        return selectedSpecialties;
    }

    // Method that handles updating a user's password. 
    private void handlePasswordUpdate() {
        String newPassword = String.valueOf(view.getPasswordField().getPassword());
        String confirmPassword = String.valueOf(view.getConfirmPasswordField().getPassword());
        if (!newPassword.equals(confirmPassword)) {
            CustomDialog.showErrorMessage(view, "Passwords do not match");
            return;
        }
        updateUserPassword(newPassword);
    }


    // Methods that updates the user's role in the database. 
    private void updateUserRole(Role selectedRole) {
        try {
            database.updateUserRole(user.getEmail(), selectedRole);
            CustomDialog.showSuccessMessage(view, "User role updated successfully!");
        } catch (SQLException ex) {
            CustomDialog.showErrorMessage(view, "Failed to update user role: " + ex.getMessage());
        }
    }

    // Method that updates the user's specialties in the database. 
    private void updateUserSpecialties(List<Category> selectedSpecialties) {
        try {
            boolean success = database.updateUserSpecialties(user.getEmail(), selectedSpecialties);
            if (success) {
                CustomDialog.showSuccessMessage(view, "User specialties updated successfully!");
            } else {
                CustomDialog.showErrorMessage(view, "Failed to update user specialties");
            }
        } catch (Exception ex) {
            CustomDialog.showErrorMessage(view, "Failed to update user specialties: " + ex.getMessage());
        }
    }

    // Method that updates the user password in the database. 
    private void updateUserPassword(String newPassword) {
        try {
            String hashedPassword = PasswordHasher.hashPassword(newPassword);
            if (hashedPassword != null) {
                database.updateUserPassword(user.getEmail(), hashedPassword);
                CustomDialog.showSuccessMessage(view, "User password updated successfully!");
            } else {
                CustomDialog.showErrorMessage(view, "Failed to update user password: Hashing algorithm not available");
            }
        } catch (Exception ex) {
            CustomDialog.showErrorMessage(view, "Failed to update user password: " + ex.getMessage());
        }
    }
}
