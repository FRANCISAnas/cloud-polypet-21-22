package com.archicloud.polypet20212022.catalogmanager.repositories;

import com.archicloud.polypet20212022.catalogmanager.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Optional<Partner> findByName(String productReference);
}
