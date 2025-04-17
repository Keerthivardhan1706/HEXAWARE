package com.hexaware.carconnect.dao.interfaces;

import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.exception.InvalidInputException;

public interface ICustomerService {
    Customer getCustomerById(int customerId) throws InvalidInputException;
    void registerCustomer(Customer customer) throws InvalidInputException;
    void updateCustomer(Customer customer) throws InvalidInputException;
    void deleteCustomer(int customerId) throws InvalidInputException;
}
