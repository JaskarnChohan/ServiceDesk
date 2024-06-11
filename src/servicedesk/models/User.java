package servicedesk.models;

import servicedesk.enums.Role;

public class User {

    private final String fullName;
    private final String email;
    private String hashedPassword;
    private final String phoneNumber;
    private final String department;
    private Role role;

    // Constructor to create a new User object
    public User(String fullName, String email, String hashedPassword, String phoneNumber, String department, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.role = role;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public Role getRole() {
        return role;
    }

    // Setters
    public void setRole(Role role) {
        this.role = role;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    // toString for User to display user information 
    @Override
    public String toString() {
        return email + "  (" + role + ")";
    }
}
