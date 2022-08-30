package com.archicloud.polypet20212022.customercare.repositories;

import com.archicloud.polypet20212022.customercare.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String customerEmail);

    boolean existsByEmail(String email);
}
