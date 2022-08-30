package com.archicloud.polypet20212022.accounting.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private static final Logger log = LoggerFactory.getLogger(Product.class.getName());

    protected String reference;

    @NotNull
    private String name;

    @NotNull
    private String category;

    @NotNull
    private String origin;

    @NotNull
    private Double price;

    @Embedded
    private Address address;

    @PrePersist
    private void prePersistInternalProduct() {
        log.info("PrePersist method called");
        if (reference == null || reference.length() == 0) {
            generateReference();
        }
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
