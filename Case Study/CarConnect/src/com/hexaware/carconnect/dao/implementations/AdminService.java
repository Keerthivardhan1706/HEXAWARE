package com.hexaware.carconnect.dao.implementations;

import com.hexaware.carconnect.dao.interfaces.IAdminService;
import com.hexaware.carconnect.entity.Admin;
import com.hexaware.carconnect.exception.AdminNotFoundException;
import com.hexaware.carconnect.exception.InvalidInputException;

import java.sql.*;

public class AdminService implements IAdminService {
    private Connection connection;

    public AdminService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Admin getAdminById(int adminId) throws AdminNotFoundException {
        Admin admin = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM Admin WHERE AdminID = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, adminId);
            rs = ps.executeQuery();

            if (rs.next()) {
                admin = mapResultSetToAdmin(rs);
            } else {
                throw new AdminNotFoundException("Admin with ID " + adminId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps);
        }

        return admin;
    }

   
    @Override
    public Admin loginAdmin(String username, String password) throws AdminNotFoundException {
        Admin admin = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                admin = mapResultSetToAdmin(rs);
            } else {
                throw new AdminNotFoundException("Invalid admin username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps);
        }

        return admin;
    }

    @Override
    public void registerAdmin(Admin admin) throws InvalidInputException {
        if (admin == null || admin.getUsername().isEmpty() || admin.getPassword().isEmpty()) {
            throw new InvalidInputException("Username and password cannot be empty.");
        }

        PreparedStatement ps = null;

        try {
            String query = "INSERT INTO Admin (Username, Password, FirstName, LastName, PhoneNumber, Email) VALUES (?, ?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getFirstName());
            ps.setString(4, admin.getLastName());
            ps.setString(5, admin.getPhoneNumber());
            ps.setString(6, admin.getEmail());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new InvalidInputException("Failed to register admin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void updateAdmin(Admin admin) throws AdminNotFoundException, InvalidInputException {
        if (admin == null || admin.getAdminID() <= 0) {
            throw new InvalidInputException("Invalid admin details.");
        }

        PreparedStatement ps = null;

        try {
            String query = "UPDATE Admin SET Username = ?, Password = ?, FirstName = ?, LastName = ?, PhoneNumber = ?, Email = ? WHERE AdminID = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getFirstName());
            ps.setString(4, admin.getLastName());
            ps.setString(5, admin.getPhoneNumber());
            ps.setString(6, admin.getEmail());
            ps.setInt(7, admin.getAdminID());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new AdminNotFoundException("Admin with ID " + admin.getAdminID() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps);
        }
    }

    @Override
    public void deleteAdmin(int adminId) throws AdminNotFoundException {
        PreparedStatement ps = null;

        try {
            String query = "DELETE FROM Admin WHERE AdminID = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, adminId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new AdminNotFoundException("Admin with ID " + adminId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps);
        }
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        return new Admin(
            rs.getInt("AdminID"),
            rs.getString("Username"),
            rs.getString("Password"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("PhoneNumber"),
            rs.getString("Email")
        );
    }

    private void closeResources(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            // Do NOT close connection here
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
