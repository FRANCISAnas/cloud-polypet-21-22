package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Product {

    @Id
    protected String reference;

    private String name;

    private String category;

    private String origin;

    private Double price;

    @PrePersist
    private void prePersistInternalProduct() {
        if (reference == null || reference.length() == 0) {
            generateReference();
        }
    }

    public String getReference() {
        if (reference == null || reference.length() == 0) {
            generateReference();
        }
        return reference;
    }

    protected void generateReference() {
        reference = getOrigin() + '_'
                + (getName() != null && getName().length() > 0 ? "base_name_" + getName() : hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Product product))
            return false;
        return getReference() != null && getReference().length() > 0
                ? Objects.equals(reference, ((Product) o).getReference())
                : Objects.equals(getName(), product.getName()) && Objects.equals(getCategory(), product.getCategory())
                        && Objects.equals(getOrigin(), product.getOrigin())
                        && Objects.equals(getPrice(), product.getPrice());
    }

    @Override
    public int hashCode() {
        return getReference() != null && getReference().length() > 0 ? Objects.hash(getReference())
                : Objects.hash(getName(), getCategory(), getOrigin(), getPrice());
    }
}
