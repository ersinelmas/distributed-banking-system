package com.dbs.customer.application.service.impl;

import com.dbs.customer.application.service.CustomerService;
import com.dbs.customer.domain.model.Customer;
import com.dbs.customer.domain.repository.CustomerRepository;
import com.dbs.customer.infrastructure.exception.AlreadyExistsException;
import com.dbs.customer.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerManager implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new AlreadyExistsException("Customer with this email already exists");
        }
        if (customerRepository.existsByTaxNumber(customer.getTaxNumber())) {
            throw new AlreadyExistsException("Customer with this tax number already exists");
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerByNumber(String customerNumber) {
        return customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with number: " + customerNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}