package com.hexaware.carconnect.dao.implementations;

import com.hexaware.carconnect.dao.interfaces.IVehicleService;
import com.hexaware.carconnect.entity.Vehicle;
import com.hexaware.carconnect.exception.VehicleNotFoundException;
import com.hexaware.carconnect.exception.InvalidInputException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleService implements IVehicleService {
    private Connection connection;

    public VehicleService(Connection connection) {
        this.connection = connection;
    }

    // Get vehicle by ID
    @Override
    public Vehicle getVehicleById(int vehicleId) throws VehicleNotFoundException {
        Vehicle vehicle = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM Vehicle WHERE VehicleID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vehicleId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                vehicle = new Vehicle(
                    resultSet.getInt("VehicleID"),
                    resultSet.getString("Model"),
                    resultSet.getString("Make"),
                    resultSet.getInt("Year"),
                    resultSet.getString("Color"),
                    resultSet.getString("RegistrationNumber"),
                    resultSet.getBoolean("Availability"),
                    resultSet.getDouble("DailyRate")
                );
            } else {
                throw new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new VehicleNotFoundException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(resultSet, preparedStatement);
        }

        return vehicle;
    }

    // Get all available vehicles
    @Override
    public List<Vehicle> getAvailableVehicles() throws VehicleNotFoundException {
        List<Vehicle> availableVehicles = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM Vehicle WHERE Availability = true";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                availableVehicles.add(new Vehicle(
                    resultSet.getInt("VehicleID"),
                    resultSet.getString("Model"),
                    resultSet.getString("Make"),
                    resultSet.getInt("Year"),
                    resultSet.getString("Color"),
                    resultSet.getString("RegistrationNumber"),
                    resultSet.getBoolean("Availability"),
                    resultSet.getDouble("DailyRate")
                ));
            }

            if (availableVehicles.isEmpty()) {
                throw new VehicleNotFoundException("No available vehicles found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new VehicleNotFoundException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(resultSet, preparedStatement);
        }

        return availableVehicles;
    }

    // Add a new vehicle
    @Override
    public void addVehicle(Vehicle vehicle) throws InvalidInputException {
        if (vehicle == null || vehicle.getModel().isEmpty() || vehicle.getMake().isEmpty()) {
            throw new InvalidInputException("Vehicle model and make cannot be empty.");
        }

        PreparedStatement preparedStatement = null;

        try {
            String query = "INSERT INTO Vehicle (Model, Make, Year, Color, RegistrationNumber, Availability, DailyRate) VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vehicle.getModel());
            preparedStatement.setString(2, vehicle.getMake());
            preparedStatement.setInt(3, vehicle.getYear());
            preparedStatement.setString(4, vehicle.getColor());
            preparedStatement.setString(5, vehicle.getRegistrationNumber());
            preparedStatement.setBoolean(6, vehicle.isAvailability());
            preparedStatement.setDouble(7, vehicle.getDailyRate());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new InvalidInputException("Failed to add vehicle. Please check the input data.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidInputException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(null, preparedStatement);
        }
    }

    // Update vehicle details
    @Override
    public void updateVehicle(Vehicle vehicle) throws VehicleNotFoundException, InvalidInputException {
        if (vehicle == null || vehicle.getVehicleID() <= 0) {
            throw new InvalidInputException("Invalid vehicle details provided.");
        }

        PreparedStatement preparedStatement = null;

        try {
            String query = "UPDATE Vehicle SET Model = ?, Make = ?, Year = ?, Color = ?, RegistrationNumber = ?, Availability = ?, DailyRate = ? WHERE VehicleID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vehicle.getModel());
            preparedStatement.setString(2, vehicle.getMake());
            preparedStatement.setInt(3, vehicle.getYear());
            preparedStatement.setString(4, vehicle.getColor());
            preparedStatement.setString(5, vehicle.getRegistrationNumber());
            preparedStatement.setBoolean(6, vehicle.isAvailability());
            preparedStatement.setDouble(7, vehicle.getDailyRate());
            preparedStatement.setInt(8, vehicle.getVehicleID());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new VehicleNotFoundException("Vehicle with ID " + vehicle.getVehicleID() + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new VehicleNotFoundException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(null, preparedStatement);
        }
    }

    // Remove vehicle by ID
    @Override
    public void removeVehicle(int vehicleId) throws VehicleNotFoundException {
        PreparedStatement preparedStatement = null;

        try {
            String query = "DELETE FROM Vehicle WHERE VehicleID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vehicleId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new VehicleNotFoundException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(null, preparedStatement);
        }
    }


    private void closeResources(ResultSet resultSet, PreparedStatement preparedStatement) {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
