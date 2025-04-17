package com.hexaware.carconnect.dao.implementations;

import com.hexaware.carconnect.dao.interfaces.IReservationService;
import com.hexaware.carconnect.entity.Reservation;
import com.hexaware.carconnect.exception.ReservationException;
import com.hexaware.carconnect.exception.InvalidInputException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservationService {

    private Connection connection;

    public ReservationService(Connection connection) {
        this.connection = connection;
    }

    // Get reservation by ID
    @Override
    public Reservation getReservationById(int reservationId) throws ReservationException {
        Reservation reservation = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM Reservation WHERE ReservationID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservationId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reservation = new Reservation(
                    resultSet.getInt("ReservationID"),
                    resultSet.getInt("CustomerID"),
                    resultSet.getInt("VehicleID"),
                    resultSet.getDate("StartDate"),
                    resultSet.getDate("EndDate"),
                    resultSet.getDouble("TotalCost")
                );
            } else {
                throw new ReservationException("Reservation with ID " + reservationId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(resultSet, preparedStatement);
        }

        return reservation;
    }

    // Get all reservations by customer ID
    @Override
    public List<Reservation> getReservationsByCustomerId(int customerId) throws ReservationException {
        List<Reservation> reservations = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM Reservation WHERE CustomerID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reservations.add(new Reservation(
                    resultSet.getInt("ReservationID"),
                    resultSet.getInt("CustomerID"),
                    resultSet.getInt("VehicleID"),
                    resultSet.getDate("StartDate"),
                    resultSet.getDate("EndDate"),
                    resultSet.getDouble("TotalCost")
                ));
            }

            if (reservations.isEmpty()) {
                throw new ReservationException("No reservations found for customer with ID " + customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(resultSet, preparedStatement);
        }

        return reservations;
    }

    // Create a new reservation
    @Override
    public void createReservation(Reservation reservation) throws InvalidInputException {
        if (reservation == null || reservation.getCustomerId() <= 0 || reservation.getVehicleId() <= 0) {
            throw new InvalidInputException("Invalid reservation details provided.");
        }

        PreparedStatement preparedStatement = null;

        try {
            String query = "INSERT INTO Reservation (CustomerID, VehicleID, StartDate, EndDate, TotalCost) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservation.getCustomerId());
            preparedStatement.setInt(2, reservation.getVehicleId());
            preparedStatement.setDate(3, new java.sql.Date(reservation.getStartDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(reservation.getEndDate().getTime()));
            preparedStatement.setDouble(5, reservation.getTotalCost());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new InvalidInputException("Failed to create reservation. Please check the input data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidInputException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(null, preparedStatement);
        }
    }

    // Update reservation details
    @Override
    public void updateReservation(Reservation reservation) throws ReservationException, InvalidInputException {
        if (reservation == null || reservation.getReservationId() <= 0) {
            throw new InvalidInputException("Invalid reservation details provided.");
        }

        PreparedStatement preparedStatement = null;

        try {
            String query = "UPDATE Reservation SET StartDate = ?, EndDate = ?, TotalCost = ? WHERE ReservationID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new java.sql.Date(reservation.getStartDate().getTime()));
            preparedStatement.setDate(2, new java.sql.Date(reservation.getEndDate().getTime()));
            preparedStatement.setDouble(3, reservation.getTotalCost());
            preparedStatement.setInt(4, reservation.getReservationId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ReservationException("Reservation with ID " + reservation.getReservationId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(null, preparedStatement);
        }
    }

    // Cancel reservation by ID
    @Override
    public void cancelReservation(int reservationId) throws ReservationException {
        PreparedStatement preparedStatement = null;

        try {
            String query = "DELETE FROM Reservation WHERE ReservationID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservationId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ReservationException("Reservation with ID " + reservationId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(null, preparedStatement);
        }
    }

    // View all reservations
    @Override
    public List<Reservation> viewAllReservations() throws ReservationException {
        List<Reservation> reservations = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM Reservation";
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                reservations.add(new Reservation(
                    rs.getInt("ReservationID"),
                    rs.getInt("CustomerID"),
                    rs.getInt("VehicleID"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getDouble("TotalCost")
                ));
            }

            if (reservations.isEmpty()) {
                throw new ReservationException("No reservations found in the system.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ReservationException("SQL Error: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt);
        }

        return reservations;
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
