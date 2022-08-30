package com.sitemonitoring.sitemonitoring.repository;

import com.sitemonitoring.sitemonitoring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesRepository extends JpaRepository<Product, String> {

    Optional<Product> findProductByReference(String ref);

}
