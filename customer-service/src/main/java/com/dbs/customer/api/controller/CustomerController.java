package com.dbs.customer.api.controller;

import com.dbs.customer.application.dto.CustomerCreateRequest;
import com.dbs.customer.application.dto.CustomerResponse;
import com.dbs.customer.application.mapper.CustomerMapper;
import com.dbs.customer.application.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        var customer = customerMapper.toEntity(request);
        var savedCustomer = customerService.createCustomer(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @GetMapping("/{customerNumber}")
    public CustomerResponse getCustomer(@PathVariable String customerNumber) {
        var customer = customerService.getCustomerByNumber(customerNumber);
        return customerMapper.toResponse(customer);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {
        var customers = customerService.getAllCustomers();
        return customerMapper.toResponseList(customers);
    }
}