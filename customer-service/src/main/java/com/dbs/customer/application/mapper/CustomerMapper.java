package com.dbs.customer.application.mapper;

import com.dbs.customer.application.dto.CustomerCreateRequest;
import com.dbs.customer.application.dto.CustomerResponse;
import com.dbs.customer.domain.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerCreateRequest request);

    CustomerResponse toResponse(Customer customer);

    List<CustomerResponse> toResponseList(List<Customer> customers);
}