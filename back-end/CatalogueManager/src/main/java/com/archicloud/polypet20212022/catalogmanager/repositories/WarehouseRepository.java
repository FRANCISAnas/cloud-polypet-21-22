package com.archicloud.polypet20212022.catalogmanager.repositories;

import com.archicloud.polypet20212022.catalogmanager.entity.ProductInventory;
import com.archicloud.polypet20212022.catalogmanager.entity.StoreHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<StoreHouse, Long> {
    Optional<StoreHouse> findByProductInventories(ProductInventory productInventories);

    Optional<StoreHouse> findByProductInventories(String productReference);

}
