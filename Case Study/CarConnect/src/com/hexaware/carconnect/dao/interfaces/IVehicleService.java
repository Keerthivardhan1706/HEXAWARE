package com.hexaware.carconnect.dao.interfaces;
import com.hexaware.carconnect.exception.VehicleNotFoundException;
import com.hexaware.carconnect.exception.InvalidInputException;
import com.hexaware.carconnect.entity.Vehicle;
import java.util.List;
public interface IVehicleService {
    Vehicle getVehicleById(int vehicleId) throws VehicleNotFoundException;
    List<Vehicle> getAvailableVehicles() throws VehicleNotFoundException;
    void addVehicle(Vehicle vehicle) throws InvalidInputException;
    void updateVehicle(Vehicle vehicle) throws VehicleNotFoundException, InvalidInputException;
    void removeVehicle(int vehicleId) throws VehicleNotFoundException;
}

