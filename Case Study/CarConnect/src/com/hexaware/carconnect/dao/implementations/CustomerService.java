package com.hexaware.carconnect.dao.implementations;

import com.hexaware.carconnect.dao.interfaces.ICustomerService;
import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.exception.InvalidInputException;
import com.hexaware.carconnect.util.InputValidator;  
import java.sql.*;

public class CustomerService implements ICustomerService {

    private Connection connection;

    public CustomerService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Customer getCustomerById(int customerId) throws InvalidInputException {
        Customer customer = null;
        try {
            String query = "SELECT * FROM Customer WHERE CustomerID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                customer = mapResultSetToCustomer(rs);
            } else {
                throw new InvalidInputException("Customer not found with ID: " + customerId);
            }
        } catch (SQLException e) {
            throw new InvalidInputException("SQL Error: " + e.getMessage());
        }
        return customer;
    }

    @Override
    public void registerCustomer(Customer customer) throws InvalidInputException {
        // Validate email and phone number before attempting to register the customer
        if (!InputValidator.isValidEmail(customer.getEmail())) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (!InputValidator.isValidPhoneNumber(customer.getPhoneNumber())) {
            throw new InvalidInputException("Phone number must be exactly 10 digits.");
        }

        try {
            String query = "INSERT INTO Customer (FirstName, LastName, Email, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new InvalidInputException("Customer registration failed.");
            }
        } catch (SQLException e) {
            throw new InvalidInputException("SQL Error: " + e.getMessage());
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws InvalidInputException {
        // Validate email and phone number before attempting to update the customer
        if (!InputValidator.isValidEmail(customer.getEmail())) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (!InputValidator.isValidPhoneNumber(customer.getPhoneNumber())) {
            throw new InvalidInputException("Phone number must be exactly 10 digits.");
        }

        try {
            String query = "UPDATE Customer SET FirstName = ?, LastName = ?, Email = ?, PhoneNumber = ?, Address = ? WHERE CustomerID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getAddress());
            pstmt.setInt(6, customer.getCustomerID());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new InvalidInputException("Update failed. Customer not found.");
            }
        } catch (SQLException e) {
            throw new InvalidInputException("SQL Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteCustomer(int customerId) throws InvalidInputException {
        try {
            String query = "DELETE FROM Customer WHERE CustomerID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, customerId);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new InvalidInputException("Delete failed. Customer not found.");
            }
        } catch (SQLException e) {
            throw new InvalidInputException("SQL Error: " + e.getMessage());
        }
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("CustomerID"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("PhoneNumber"),
            rs.getString("Address"),
            rs.getTimestamp("RegistrationDate")
        );
    }
}
