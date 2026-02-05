package com.dbs.customer.domain.repository;

import com.dbs.customer.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerNumber(String customerNumber);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByTaxNumber(String taxNumber);
    boolean existsByEmail(String email);
    boolean existsByTaxNumber(String taxNumber);
}