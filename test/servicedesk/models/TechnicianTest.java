package servicedesk.models;

import servicedesk.enums.Category;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

// Testing the Technician class 
public class TechnicianTest {

    private Technician technician;

    // Initial setup to make the test work 
    @Before
    public void setUp() {
        // Create a technician object for testing
        List<Category> specialties = new ArrayList<>();
        specialties.add(Category.HARDWARE);
        specialties.add(Category.SOFTWARE);
        technician = new Technician("Jane Smith", "jane@example.com", "hashedPassword", "9876543210", "IT", specialties);
    }

    @Test
    public void testGetSpecialties() {
        // Test getting specialties
        List<Category> expectedSpecialties = new ArrayList<>();
        expectedSpecialties.add(Category.HARDWARE);
        expectedSpecialties.add(Category.SOFTWARE);
        assertEquals(expectedSpecialties, technician.getSpecialties());
    }

    @Test
    public void testAddSpecialty() {
        // Test adding a specialty
        technician.addSpecialty(Category.NETWORK);
        assertTrue(technician.getSpecialties().contains(Category.NETWORK));
    }

    @Test
    public void testToString() {
        // Test toString method
        String expectedToString = "jane@example.com  (TECHNICIAN)\nSpecialties:\n- HARDWARE\n- SOFTWARE";
        assertEquals(expectedToString, technician.toString());
    }
}
