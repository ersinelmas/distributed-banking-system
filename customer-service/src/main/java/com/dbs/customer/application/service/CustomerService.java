package com.dbs.customer.application.service;

import com.dbs.customer.domain.model.Customer;
import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomerByNumber(String customerNumber);
    List<Customer> getAllCustomers();
}