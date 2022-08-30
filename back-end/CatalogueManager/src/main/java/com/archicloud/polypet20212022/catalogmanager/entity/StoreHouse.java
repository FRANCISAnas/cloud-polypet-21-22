package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class StoreHouse {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @ElementCollection
    private List<ProductInventory> productInventories;

    public StoreHouse(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public StoreHouse() {

    }

    public Integer incrementProductQuantity(Product product, Integer quantity) {
        return setProductQuantity(product, quantity);
    }

    public Integer decrementProductQuantity(Product product, Integer quantity) {
        return setProductQuantity(product, -quantity);
    }

    private ProductInventory getProductInventoryByProductReference(String reference) {
        for (ProductInventory productInventory : getProductInventories())
            if (Objects.equals(productInventory.getReference(), reference)) {
                return productInventory;
            }
        return null;
    }

    public Integer getProductQuantity(String reference) {
        var productInventory = getProductInventoryByProductReference(reference);
        if (productInventory != null)
            return productInventory.getQuantity();
        return -1;
    }

    private Integer setProductQuantity(Product product, Integer quantity) {
        if (getProductInventories() == null) {
            productInventories = new ArrayList<>();
            productInventories.add(new ProductInventory(product, Math.max(quantity, 0)));
            return quantity;
        }
        var productInventory = getProductInventoryByProductReference(product.getReference());
        if (productInventory != null) {
            var q = productInventory.getQuantity();
            productInventory.setQuantity(Math.max(q + quantity, 0));
            return productInventory.getQuantity();
        } else {
            productInventories.add(new ProductInventory(product, Math.max(quantity, 0)));
            return quantity;
        }
    }
}
