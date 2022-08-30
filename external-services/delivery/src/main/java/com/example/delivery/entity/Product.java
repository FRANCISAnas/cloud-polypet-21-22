package com.example.delivery.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Product {
    private static final Logger log = LoggerFactory.getLogger(Product.class.getName());

    @Id
    protected String reference;

    private String name;

    private String category;

    private String origin;

    private Double price;

    @PrePersist
    private void prePersistInternalProduct() {
        log.info("PrePersist method called");
        if (reference == null || reference.length() == 0) {
            generateReference();
        }
    }

    public String getReference() {
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
