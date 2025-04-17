package com.hexaware.carconnect.entity;

import java.sql.Date;

public class Reservation {
    private int reservationId;
    private int customerId;
    private int vehicleId;
    private Date startDate;
    private Date endDate;
    private double totalCost;

    // Constructor
    public Reservation(int reservationId, int customerId, int vehicleId, Date startDate, Date endDate, double totalCost) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + 
               ", Customer ID: " + customerId + 
               ", Vehicle ID: " + vehicleId + 
               ", Start Date: " + startDate + 
               ", End Date: " + endDate + 
               ", Total Cost: " + totalCost;
    }
}
