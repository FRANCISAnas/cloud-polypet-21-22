package com.archicloud.polypet20212022.catalogmanager.repositories;

import com.archicloud.polypet20212022.catalogmanager.entity.PartnerProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerProductRepository extends JpaRepository<PartnerProduct, Long> {
    Optional<PartnerProduct> findByReference(String name);

    List<PartnerProduct> findAllByCategory(String category);

    List<PartnerProduct> findByNameContaining(String title);
}
