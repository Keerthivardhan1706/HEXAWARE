package com.hexaware.carconnect.test;



import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

import org.junit.jupiter.api.*;

import com.hexaware.carconnect.dao.implementations.CustomerService;
import com.hexaware.carconnect.dao.interfaces.ICustomerService;
import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.exception.InvalidInputException;
import com.hexaware.carconnect.util.DatabaseConnection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceTest {

    static ICustomerService customerService;
    static Connection conn;
    static int customerId;
    static String email;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        conn = DatabaseConnection.getConnection("Resources/db.properties");
        customerService = new CustomerService(conn);
    }

    @BeforeEach
    void logCustomerId() {
        System.out.println("Current customerId: " + customerId);
    }

    @Test
    @Order(1)
    void testRegisterCustomer() throws Exception {
        Customer customer = new Customer(0, "Test", "User", "testuser@example.com", "9999999999", "Test City", null);

        customerService.registerCustomer(customer);

        String query = "SELECT * FROM Customer WHERE Email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, customer.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customerId = rs.getInt("CustomerID");
                    email = rs.getString("Email");
                }
            }
        }

        System.out.println("Customer registered with ID: " + customerId);
        assertTrue(customerId > 0);
    }

    @Test
    @Order(2)
    void testGetCustomerById() {
        assertDoesNotThrow(() -> {
            Customer customer = customerService.getCustomerById(customerId);
            assertNotNull(customer);
            assertEquals(email, customer.getEmail());
        });
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        Customer updated = new Customer(customerId, "Updated", "Name", "updated@example.com", "8888888888", "Updated City", null);
        assertDoesNotThrow(() -> customerService.updateCustomer(updated));
    }

    @Test
    @Order(4)
    void testDeleteCustomer() {
        assertDoesNotThrow(() -> customerService.deleteCustomer(customerId));
        assertThrows(InvalidInputException.class, () -> customerService.getCustomerById(customerId));
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
