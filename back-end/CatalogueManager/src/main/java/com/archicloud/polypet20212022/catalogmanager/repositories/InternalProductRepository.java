package com.archicloud.polypet20212022.catalogmanager.repositories;

import java.util.List;
import java.util.Optional;

import com.archicloud.polypet20212022.catalogmanager.entity.InternalProduct;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InternalProductRepository extends JpaRepository<InternalProduct, Long> {
    Optional<InternalProduct> findByReference(String name);

    boolean existsByCategory(String category);

    List<InternalProduct> findAllByCategory(String category);

    boolean existsByReference(String productReference);

    List<InternalProduct> findByNameContaining(String title);
}
