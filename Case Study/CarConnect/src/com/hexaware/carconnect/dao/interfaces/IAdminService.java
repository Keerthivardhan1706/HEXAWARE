package com.hexaware.carconnect.dao.interfaces;

import com.hexaware.carconnect.entity.Admin;
import com.hexaware.carconnect.exception.AdminNotFoundException;
import com.hexaware.carconnect.exception.InvalidInputException;

public interface IAdminService {
    
    Admin getAdminById(int adminId) throws AdminNotFoundException;
    
   
    
    void registerAdmin(Admin admin) throws InvalidInputException;
    
    void updateAdmin(Admin admin) throws AdminNotFoundException, InvalidInputException;
    
    void deleteAdmin(int adminId) throws AdminNotFoundException;


    Admin loginAdmin(String username, String password) throws InvalidInputException, AdminNotFoundException;
}
