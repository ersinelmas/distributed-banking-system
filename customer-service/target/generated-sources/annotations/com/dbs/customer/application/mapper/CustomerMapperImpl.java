package com.dbs.customer.application.mapper;

import com.dbs.customer.application.dto.CustomerCreateRequest;
import com.dbs.customer.application.dto.CustomerResponse;
import com.dbs.customer.domain.model.Customer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-06T01:12:47+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.firstName( request.firstName() );
        customer.lastName( request.lastName() );
        customer.email( request.email() );
        customer.taxNumber( request.taxNumber() );

        return customer.build();
    }

    @Override
    public CustomerResponse toResponse(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        String customerNumber = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String status = null;
        LocalDateTime createdAt = null;

        customerNumber = customer.getCustomerNumber();
        firstName = customer.getFirstName();
        lastName = customer.getLastName();
        email = customer.getEmail();
        if ( customer.getStatus() != null ) {
            status = customer.getStatus().name();
        }
        createdAt = customer.getCreatedAt();

        CustomerResponse customerResponse = new CustomerResponse( customerNumber, firstName, lastName, email, status, createdAt );

        return customerResponse;
    }

    @Override
    public List<CustomerResponse> toResponseList(List<Customer> customers) {
        if ( customers == null ) {
            return null;
        }

        List<CustomerResponse> list = new ArrayList<CustomerResponse>( customers.size() );
        for ( Customer customer : customers ) {
            list.add( toResponse( customer ) );
        }

        return list;
    }
}
