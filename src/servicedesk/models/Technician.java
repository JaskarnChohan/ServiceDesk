package servicedesk.models;

import servicedesk.enums.Category;
import servicedesk.enums.Role;
import java.util.List;

public class Technician extends User {

    private List<Category> specialties;

    // Constructor for a new technician. 
    public Technician(String fullName, String email, String hashedPassword, String phoneNumber, String department, List<Category> specialties) {
        super(fullName, email, hashedPassword, phoneNumber, department, Role.TECHNICIAN);
        this.specialties = specialties;
    }
    
        public List<Category> getSpecialties() {
        return specialties;
    }

    public void addSpecialty(Category specialty) {
        specialties.add(specialty);
    }

    // Returns a string representation of technician with specialties. 
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("\nSpecialties:");
        for (Category specialty : specialties) {
            builder.append("\n- ").append(specialty);
        }
        return builder.toString();
    }
}
