package com.archicloud.polypet20212022.customercare.repositories;

import com.archicloud.polypet20212022.customercare.entity.PackagesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<PackagesOrder, String> {
    Optional<PackagesOrder> findBytrackingNumber(String customerEmail);

    boolean existsBytrackingNumber(String email);
}
