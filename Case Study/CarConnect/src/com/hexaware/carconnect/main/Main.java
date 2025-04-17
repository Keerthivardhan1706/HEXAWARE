package com.hexaware.carconnect.main;

import com.hexaware.carconnect.dao.implementations.*;
import com.hexaware.carconnect.entity.*;
import com.hexaware.carconnect.exception.InvalidInputException;
import com.hexaware.carconnect.util.DatabaseConnection;
import java.sql.Timestamp;
import java.sql.Date;

import java.sql.Connection;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String fileName = "Resources/db.properties";
        try (Connection conn = DatabaseConnection.getConnection(fileName)) {

        
            CustomerService customerService = new CustomerService(conn);
            VehicleService vehicleService = new VehicleService(conn);
            ReservationService reservationService = new ReservationService(conn);
            AdminService adminService = new AdminService(conn);

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n--- Welcome to CarConnect ---");
                System.out.println("1. Customer");
                System.out.println("2. Vehicle");
                System.out.println("3. Reservation");
                System.out.println("4. Admin");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                case 1:
                  
                    System.out.println("\n--- Customer Menu ---");
                    System.out.println("1. Get Customer By ID");
                    System.out.println("2. Register New Customer");
                    System.out.println("3. Update Customer");
                    System.out.println("4. Delete Customer");
                    System.out.print("Enter choice: ");
                    int customerChoice = scanner.nextInt();
                    scanner.nextLine(); 

                    switch (customerChoice) {
                        case 1:
                          
                            System.out.print("Enter Customer ID: ");
                            int customerId = scanner.nextInt();
                            try {
                                Customer customer = customerService.getCustomerById(customerId);
                                System.out.println(customer);
                            } catch (InvalidInputException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;

                        case 2:
                           
                            System.out.print("Enter First Name: ");
                            String firstName = scanner.nextLine();
                            System.out.print("Enter Last Name: ");
                            String lastName = scanner.nextLine();
                            System.out.print("Enter Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Enter Phone Number: ");
                            String phone = scanner.nextLine();
                            System.out.print("Enter Address: ");
                            String address = scanner.nextLine();

                           
                            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                                System.out.println("Error: All fields are required.");
                                break;
                            }

                           
                            Customer newCustomer = new Customer(firstName, lastName, email, phone, address);
                            try {
                                customerService.registerCustomer(newCustomer);
                                System.out.println("Customer registered successfully.");
                            } catch (InvalidInputException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;

                        case 3:
                           
                            System.out.print("Enter Customer ID to update: ");
                            int updateCustomerId = scanner.nextInt();
                            scanner.nextLine(); 
                            System.out.print("Enter new First Name: ");
                            String updateFirstName = scanner.nextLine();
                            System.out.print("Enter new Last Name: ");
                            String updateLastName = scanner.nextLine();
                            System.out.print("Enter new Email: ");
                            String updateEmail = scanner.nextLine();
                            System.out.print("Enter new Phone Number: ");
                            String updatePhone = scanner.nextLine();
                            System.out.print("Enter new Address: ");
                            String updateAddress = scanner.nextLine();


                            if (updateFirstName.isEmpty() || updateLastName.isEmpty() || updateEmail.isEmpty() || updatePhone.isEmpty() || updateAddress.isEmpty()) {
                                System.out.println("Error: All fields are required.");
                                break;
                            }

                            Customer updatedCustomer = new Customer(updateCustomerId, updateFirstName, updateLastName, updateEmail, updatePhone, updateAddress, new Timestamp(System.currentTimeMillis()));
                            try {
                                customerService.updateCustomer(updatedCustomer);
                                System.out.println("Customer updated successfully.");
                            } catch (InvalidInputException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;

                        case 4:
                            System.out.print("Enter Customer ID to delete: ");
                            int deleteCustomerId = scanner.nextInt();
                            try {
                                customerService.deleteCustomer(deleteCustomerId);
                                System.out.println("Customer deleted successfully.");
                            } catch (InvalidInputException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;


                    case 2:
                       
                        System.out.println("\n--- Vehicle Menu ---");
                        System.out.println("1. Get Vehicle By ID");
                        System.out.println("2. View Available Vehicles");
                        System.out.println("3. Add New Vehicle");
                        System.out.println("4. Update Vehicle");
                        System.out.println("5. Remove Vehicle");
                        System.out.print("Enter choice: ");
                        int vehicleChoice = scanner.nextInt();
                        scanner.nextLine(); 

                        switch (vehicleChoice) {
                            case 1:
                               
                                System.out.print("Enter Vehicle ID: ");
                                int vehicleId = scanner.nextInt();
                                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
                                System.out.println(vehicle);
                                break;

                            case 2:
                               
                                List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
                                if (availableVehicles.isEmpty()) {
                                    System.out.println("No vehicles available.");
                                } else {
                                    System.out.println("Available Vehicles:");
                                    for (Vehicle v : availableVehicles) {
                                        System.out.println(v);
                                    }
                                }
                                break;

                            case 3:
                               
                                System.out.print("Enter Model: ");
                                String model = scanner.nextLine();
                                System.out.print("Enter Make: ");
                                String make = scanner.nextLine();
                                System.out.print("Enter Year: ");
                                int year = scanner.nextInt();
                                scanner.nextLine(); 
                                System.out.print("Enter Color: ");
                                String color = scanner.nextLine();
                                System.out.print("Enter Registration Number: ");
                                String regNumber = scanner.nextLine();
                                System.out.print("Is Available (true/false): ");
                                boolean available = scanner.nextBoolean();
                                System.out.print("Enter Daily Rate: ");
                                double rate = scanner.nextDouble();

                                Vehicle newVehicle = new Vehicle(0, model, make, year, color, regNumber, available, rate);
                                vehicleService.addVehicle(newVehicle);
                                System.out.println("Vehicle added successfully.");
                                break;

                            case 4:
                              
                                System.out.print("Enter Vehicle ID to update: ");
                                int updateVehicleId = scanner.nextInt();
                                scanner.nextLine(); 
                                System.out.print("Enter new Model: ");
                                String updateModel = scanner.nextLine();
                                System.out.print("Enter new Make: ");
                                String updateMake = scanner.nextLine();
                                System.out.print("Enter new Year: ");
                                int updateYear = scanner.nextInt();
                                scanner.nextLine(); 
                                System.out.print("Enter new Color: ");
                                String updateColor = scanner.nextLine();
                                System.out.print("Enter new Registration Number: ");
                                String updateRegNumber = scanner.nextLine();
                                System.out.print("Is Available (true/false): ");
                                boolean updateAvailable = scanner.nextBoolean();
                                System.out.print("Enter new Daily Rate: ");
                                double updateRate = scanner.nextDouble();

                                Vehicle updatedVehicle = new Vehicle(updateVehicleId, updateModel, updateMake, updateYear, updateColor, updateRegNumber, updateAvailable, updateRate);
                                vehicleService.updateVehicle(updatedVehicle);
                                System.out.println("Vehicle updated successfully.");
                                break;

                            case 5:
                               
                                System.out.print("Enter Vehicle ID to remove: ");
                                int removeVehicleId = scanner.nextInt();
                                vehicleService.removeVehicle(removeVehicleId);
                                System.out.println("Vehicle removed successfully.");
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                        break;

                    case 3:
                       
                        System.out.println("\n--- Reservation Menu ---");
                        System.out.println("1. Get Reservation By ID");
                        System.out.println("2. Get Reservations By Customer ID");
                        System.out.println("3. Create Reservation");
                        System.out.println("4. Update Reservation");
                        System.out.println("5. Cancel Reservation");
                        System.out.print("Enter choice: ");
                        int reservationChoice = scanner.nextInt();
                        scanner.nextLine(); 

                        switch (reservationChoice) {
                            case 1:
                               
                                System.out.print("Enter Reservation ID: ");
                                int reservationId = scanner.nextInt();
                                Reservation reservation = reservationService.getReservationById(reservationId);
                                System.out.println(reservation);
                                break;

                            case 2:
                               
                                System.out.print("Enter Customer ID: ");
                                int customerReservationId = scanner.nextInt();
                                List<Reservation> reservations = reservationService.getReservationsByCustomerId(customerReservationId);
                                if (reservations.isEmpty()) {
                                    System.out.println("No reservations found.");
                                } else {
                                    System.out.println("Reservations:");
                                    for (Reservation r : reservations) {
                                        System.out.println(r);
                                    }
                                }
                                break;

                            case 3:
                               
                                System.out.print("Enter Customer ID: ");
                                int customerIdForReservation = scanner.nextInt();
                                System.out.print("Enter Vehicle ID: ");
                                int vehicleIdForReservation = scanner.nextInt();
                                scanner.nextLine(); 
                                System.out.print("Enter Start Date (YYYY-MM-DD): ");
                                String startDateStr = scanner.nextLine();
                                System.out.print("Enter End Date (YYYY-MM-DD): ");
                                String endDateStr = scanner.nextLine();

                               
                                Date startDate = Date.valueOf(startDateStr);
                                Date endDate = Date.valueOf(endDateStr);

                                Vehicle vehicleForReservation = vehicleService.getVehicleById(vehicleIdForReservation);
                                long duration = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24); // in days
                                double totalAmount = duration * vehicleForReservation.getDailyRate();

                                Reservation newReservation = new Reservation(0, customerIdForReservation, vehicleIdForReservation, startDate, endDate, totalAmount);
                                reservationService.createReservation(newReservation);
                                System.out.println("Reservation created successfully. Total amount: $" + totalAmount);
                                break;


                            case 4:
                               
                                System.out.print("Enter Reservation ID to update: ");
                                int updateReservationId = scanner.nextInt();
                                scanner.nextLine(); 

                                
                                System.out.print("Enter Customer ID: ");
                                int customerId = scanner.nextInt();
                                scanner.nextLine(); 

                                System.out.print("Enter Vehicle ID: ");
                                int vehicleId = scanner.nextInt();
                                scanner.nextLine(); 

                                System.out.print("Enter new Start Date (YYYY-MM-DD): ");
                                String newStartDateStr = scanner.nextLine();
                                System.out.print("Enter new End Date (YYYY-MM-DD): ");
                                String newEndDateStr = scanner.nextLine();

                              
                                Date newStartDate = Date.valueOf(newStartDateStr);
                                Date newEndDate = Date.valueOf(newEndDateStr);

                              
                                double totalCost = calculateTotalAmount(newStartDate, newEndDate, vehicleId); 
                             
                                Reservation updatedReservation = new Reservation(updateReservationId, customerId, vehicleId, newStartDate, newEndDate, totalCost);

                                reservationService.updateReservation(updatedReservation);
                                System.out.println("Reservation updated successfully.");
                                break;


                            case 5:
                               
                                System.out.print("Enter Reservation ID to cancel: ");
                                int cancelReservationId = scanner.nextInt();
                                reservationService.cancelReservation(cancelReservationId);
                                System.out.println("Reservation canceled successfully.");
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                        break;

                    case 4:
                        
                        System.out.println("\n--- Admin Menu ---");
                        System.out.println("1. Get Admin By ID");
                        
                        System.out.println("2. Register Admin");
                        System.out.println("3. Update Admin");
                        System.out.println("4. Delete Admin");
                        System.out.print("Enter choice: ");
                        int adminChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (adminChoice) {
                            case 1:
                              
                                System.out.print("Enter Admin ID: ");
                                int adminId = scanner.nextInt();
                                Admin admin = adminService.getAdminById(adminId);
                                System.out.println(admin);
                                break;

                            
                            case 2:
                                
                                System.out.print("Enter Username: ");
                                String adminUsername = scanner.nextLine();
                                System.out.print("Enter Password: ");
                                String password = scanner.nextLine();
                                System.out.print("Enter First Name: ");
                                String firstName = scanner.nextLine();
                                System.out.print("Enter Last Name: ");
                                String lastName = scanner.nextLine();
                                System.out.print("Enter Phone Number: ");
                                String phoneNumber = scanner.nextLine();
                                System.out.print("Enter Email: ");
                                String email = scanner.nextLine();

                                Admin newAdmin = new Admin(0, adminUsername, password, firstName, lastName, phoneNumber, email);  // Full constructor
                                adminService.registerAdmin(newAdmin);
                                System.out.println("Admin registered successfully.");
                                break;


                            case 3:
                             
                                System.out.print("Enter Admin ID to update: ");
                                int updateAdminId = scanner.nextInt();
                                scanner.nextLine();
                                System.out.print("Enter new Username: ");
                                String updateAdminUsername = scanner.nextLine();
                                System.out.print("Enter new Password: ");
                                String updatePassword = scanner.nextLine();
                                System.out.print("Enter new First Name: ");
                                String updateFirstName = scanner.nextLine();
                                System.out.print("Enter new Last Name: ");
                                String updateLastName = scanner.nextLine();
                                System.out.print("Enter new Phone Number: ");
                                String updatePhoneNumber = scanner.nextLine();
                                System.out.print("Enter new Email: ");
                                String updateEmail = scanner.nextLine();

                                Admin updatedAdmin = new Admin(updateAdminId, updateAdminUsername, updatePassword, updateFirstName, updateLastName, updatePhoneNumber, updateEmail);
                                adminService.updateAdmin(updatedAdmin);
                                System.out.println("Admin updated successfully.");
                                break;


                            case 4:
                              
                                System.out.print("Enter Admin ID to delete: ");

                                int deleteAdminId = scanner.nextInt();
                                adminService.deleteAdmin(deleteAdminId);
                                System.out.println("Admin deleted successfully.");
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } while (choice != 5);
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

	private static double calculateTotalAmount(Date newStartDate, Date newEndDate, int vehicleId) {
		// TODO Auto-generated method stub
		return 0;
	}
}
