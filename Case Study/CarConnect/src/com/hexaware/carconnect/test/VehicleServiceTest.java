package com.hexaware.carconnect.test;

import com.hexaware.carconnect.dao.implementations.VehicleService;
import com.hexaware.carconnect.dao.interfaces.IVehicleService;
import com.hexaware.carconnect.entity.Vehicle;
import com.hexaware.carconnect.exception.VehicleNotFoundException;
import com.hexaware.carconnect.util.DatabaseConnection;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehicleServiceTest {

    private static Connection connection;
    private static IVehicleService vehicleService;
    private static int createdVehicleId;

    @BeforeAll
    public static void setUp() throws Exception {
        connection = DatabaseConnection.getConnection("Resources/db.properties");
        vehicleService = new VehicleService(connection);
    }

    @Test
    @Order(1)
    public void testAddVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("Civic");
        vehicle.setMake("Honda");
        vehicle.setYear(2021);
        vehicle.setColor("Red");
        vehicle.setRegistrationNumber("TN-99-ZZ-7890");
        vehicle.setAvailability(true);
        vehicle.setDailyRate(2500.0);

        assertDoesNotThrow(() -> {
            vehicleService.addVehicle(vehicle);

            // Fetch ID of last inserted vehicle (assuming it's the latest)
            String sql = "SELECT VehicleID FROM Vehicle WHERE RegistrationNumber = ? ORDER BY VehicleID DESC LIMIT 1";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, "TN-99-ZZ-7890");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        createdVehicleId = rs.getInt("VehicleID");
                        System.out.println("Created Vehicle ID: " + createdVehicleId);
                        assertTrue(createdVehicleId > 0);
                    } else {
                        fail("Vehicle not found after insertion.");
                    }
                }
            }
        });
    }

    @Test
    @Order(2)
    public void testUpdateVehicle() {
        Vehicle vehicle = new Vehicle(createdVehicleId, "City", "Honda", 2022, "Blue", "TN-99-ZZ-7890", true, 2700.0);
        assertDoesNotThrow(() -> vehicleService.updateVehicle(vehicle));
    }

    @Test
    @Order(3)
    public void testGetAvailableVehicles() {
        assertDoesNotThrow(() -> {
            List<Vehicle> available = vehicleService.getAvailableVehicles();
            assertNotNull(available);
            assertTrue(available.stream().anyMatch(v -> v.getVehicleID() == createdVehicleId));
        });
    }

    @Test
    @Order(4)
    public void testGetAllVehicles() {
        assertDoesNotThrow(() -> {
            String sql = "SELECT * FROM Vehicle";
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                assertTrue(rs.next());  // At least one vehicle should exist
                System.out.println("Total Vehicles Present: Data fetched successfully.");
            }
        });
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (createdVehicleId > 0) {
            try {
                vehicleService.removeVehicle(createdVehicleId);
                System.out.println("Test vehicle with ID " + createdVehicleId + " deleted.");
            } catch (VehicleNotFoundException e) {
                System.err.println("Cleanup Failed: " + e.getMessage());
            }
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
