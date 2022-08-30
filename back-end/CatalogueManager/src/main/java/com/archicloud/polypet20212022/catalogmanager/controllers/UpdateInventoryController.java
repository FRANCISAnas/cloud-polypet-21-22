package com.archicloud.polypet20212022.catalogmanager.controllers;

import com.archicloud.polypet20212022.catalogmanager.entity.Address;
import com.archicloud.polypet20212022.catalogmanager.entity.InternalProduct;
import com.archicloud.polypet20212022.catalogmanager.entity.PartnerProduct;
import com.archicloud.polypet20212022.catalogmanager.entity.ProductInventory;
import com.archicloud.polypet20212022.catalogmanager.services.InventoryWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UpdateInventoryController {
    private static final Logger log = LoggerFactory.getLogger(UpdateInventoryController.class.getName());

    @Autowired
    InventoryWs inventoryWs;

    @RequestMapping(value = { "/product/partner/" }, method = { RequestMethod.POST, RequestMethod.PUT })
    public PartnerProduct updatePartnerProduct(@Valid @RequestBody PartnerProduct partnerProduct) {
        return inventoryWs.updateProduct(partnerProduct);
    }

    @PostMapping(value = { "/products/partner/" })
    public List<PartnerProduct> createPartnerProducts(@Valid @RequestBody List<PartnerProduct> partnerProducts) {
        try {
            return inventoryWs.updateProduct(partnerProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @RequestMapping(value = { "/product/internal/" }, method = { RequestMethod.POST, RequestMethod.PUT })
    public InternalProduct updateInternalProduct(@Valid @RequestBody InternalProduct internalProduct) {
        return inventoryWs.updateProduct(internalProduct);
    }

    @PostMapping(value = { "/cart/book" })
    public boolean bookCart(@Valid @RequestBody List<ProductInventory> productInventories) {
        return inventoryWs.bookCart(productInventories);
    }

    @PostMapping(value = { "/cart/validate" })
    public boolean validateCart(@Valid @RequestBody List<ProductInventory> productInventories) {
        return inventoryWs.validateCart(productInventories);
    }

    @GetMapping(value = { "/product/{reference}/quantity" })
    public Integer storeInternalProduct(@PathVariable("reference") String reference) {
        return inventoryWs.getQuantity(reference);
    }

    @GetMapping(value = { "/product/{reference}/internal/store/{storeId}/{quantity}" })
    public Integer storeInternalProduct(@PathVariable("reference") String reference,
            @PathVariable("storeId") Long storeId, @PathVariable("quantity") Integer quantity) {
        return inventoryWs.storeProduct(reference, storeId, quantity);
    }

    @PostMapping(value = { "/store/{name}" })
    public Long storeInternalProduct(@PathVariable("name") String name, @RequestBody Address address) {
        try {
            return inventoryWs.createStore(name, address);
        } catch (Exception e) {
            e.printStackTrace();
            return (long) -1;
        }
    }

    @GetMapping(value = { "/store/available" })
    public Long availableInternalStore() {
        try {
            return inventoryWs.getAvailableInternalStore();
        } catch (Exception e) {
            e.printStackTrace();
            return (long) -1;
        }
    }
}
