package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Embeddable
@Getter
@Setter
public class ProductInventory {
    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    private Integer quantity;

    public ProductInventory() {

    }

    public ProductInventory(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getReference() {
        return product.getReference();
    }
}
