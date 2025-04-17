package com.hexaware.carconnect.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    static Connection connection = null;

 
    public static Connection getConnection(String fileName) throws Exception {
        try {
          
            String conn = DBPropertiesFile.getConnectionString(fileName);
            String[] pair = conn.split("\\|");
            String url = pair[0];
            String user = pair[1];
            String pass = pair[2];
            String driver = pair[3];

            Class.forName(driver);

           
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Established Successfully");

        } catch (SQLException e) {
            
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
           
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
          
            System.err.println("An error occurred while getting the database connection: " + e.getMessage());
            e.printStackTrace();
        }

        return connection;
    }

   
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed successfully");
            }
        } catch (SQLException e) {
          
            System.err.println("Error while closing the database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
