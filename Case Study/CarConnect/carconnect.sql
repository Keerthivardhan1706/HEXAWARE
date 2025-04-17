-- Create the database
CREATE DATABASE IF NOT EXISTS carconnect;
USE carconnect;

-- Table: admin
CREATE TABLE admin (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    PhoneNumber VARCHAR(15),
    Role VARCHAR(50),
    JoinDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Table: customer
CREATE TABLE customer (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    PhoneNumber VARCHAR(15),
    Address TEXT,
    RegistrationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: vehicle
CREATE TABLE vehicle (
    VehicleID INT AUTO_INCREMENT PRIMARY KEY,
    Model VARCHAR(100) NOT NULL,
    Make VARCHAR(100) NOT NULL,
    Year INT NOT NULL,
    Color VARCHAR(30),
    RegistrationNumber VARCHAR(50) NOT NULL,
    Availability TINYINT(1) DEFAULT 1,
    DailyRate DECIMAL(10,2) NOT NULL
);

-- Table: reservation
CREATE TABLE reservation (
    ReservationID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT NOT NULL,
    VehicleID INT NOT NULL,
    StartDate DATETIME NOT NULL,
    EndDate DATETIME NOT NULL,
    TotalCost DECIMAL(10,2) NOT NULL,
    Status VARCHAR(20),
    FOREIGN KEY (CustomerID) REFERENCES customer(CustomerID),
    FOREIGN KEY (VehicleID) REFERENCES vehicle(VehicleID)
);
