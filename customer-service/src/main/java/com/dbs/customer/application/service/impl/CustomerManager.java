package com.dbs.customer.application.service.impl;

import com.dbs.customer.application.dto.CustomerResponse;
import com.dbs.customer.application.service.CustomerService;
import com.dbs.customer.domain.event.CustomerCreatedEvent;
import com.dbs.customer.domain.model.Customer;
import com.dbs.customer.domain.repository.CustomerRepository;
import com.dbs.customer.domain.repository.elastic.CustomerElasticRepository;
import com.dbs.customer.infrastructure.exception.AlreadyExistsException;
import com.dbs.customer.infrastructure.exception.ResourceNotFoundException;
import com.dbs.customer.infrastructure.kafka.producer.CustomerEventProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerManager implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEventProducer customerEventProducer;

    @Lazy
    private final CustomerElasticRepository elasticRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"customers", "customerList"}, allEntries = true)
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new AlreadyExistsException("Customer with this email already exists");
        }
        if (customerRepository.existsByTaxNumber(customer.getTaxNumber())) {
            throw new AlreadyExistsException("Customer with this tax number already exists");
        }

        Customer savedCustomer = customerRepository.save(customer);

        CustomerCreatedEvent event = new CustomerCreatedEvent(
                savedCustomer.getCustomerNumber(),
                savedCustomer.getEmail(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedCustomer.getCreatedAt()
        );

        customerEventProducer.sendCustomerCreatedEvent(event);

        return savedCustomer;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "customers", key = "#customerNumber")
    public Customer getCustomerByNumber(String customerNumber) {
        return customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with number: " + customerNumber));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "customerList")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @CircuitBreaker(name = "customerSearchCB", fallbackMethod = "searchCustomersFallback")
    public List<CustomerResponse> searchCustomers(String query) {
        return elasticRepository.findByFirstNameContainingOrLastNameContaining(query, query)
                .stream()
                .map(index -> new CustomerResponse(
                        index.getCustomerNumber(),
                        index.getFirstName(),
                        index.getLastName(),
                        index.getEmail(),
                        "ACTIVE",
                        null
                )).toList();
    }

    public List<CustomerResponse> searchCustomersFallback(String query, Throwable t) {
        log.error("Circuit breaker active! ElasticSearch search failed. Query: {}, Reason: {}", query, t.getMessage());
        return List.of();
    }
}