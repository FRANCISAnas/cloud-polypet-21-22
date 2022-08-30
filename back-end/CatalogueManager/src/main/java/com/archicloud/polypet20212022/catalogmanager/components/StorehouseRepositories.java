package com.archicloud.polypet20212022.catalogmanager.components;

import com.archicloud.polypet20212022.catalogmanager.entity.ProductInventory;
import com.archicloud.polypet20212022.catalogmanager.entity.StoreHouse;
import com.archicloud.polypet20212022.catalogmanager.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class StorehouseRepositories {
    @Autowired
    WarehouseRepository warehouseRepository;

    public Optional<StoreHouse> findByProductInventories(String productReference) {
        for (StoreHouse storeHouse : warehouseRepository.findAll()) {
            for (ProductInventory productInventory : storeHouse.getProductInventories()) {
                if (Objects.equals(productInventory.getReference(), productReference))
                    return Optional.of(storeHouse);
            }
        }
        return Optional.empty();
    }

    public Optional<StoreHouse> findByProductInventories(ProductInventory productInventories) {
        return findByProductInventories(productInventories.getReference());
    }
}
