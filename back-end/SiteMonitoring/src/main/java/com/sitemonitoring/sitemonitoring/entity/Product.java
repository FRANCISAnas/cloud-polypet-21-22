package com.sitemonitoring.sitemonitoring.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product {
    private static final Logger logger = LoggerFactory.getLogger(Product.class.getName());

    @Id
    protected String reference;

    private long visits;

    private long bought;

    public long getVisits() {
        return visits;
    }

    public void setVisits(long visits) {
        this.visits = visits;
    }

    public long getBought() {
        return bought;
    }

    public void setBought(long bought) {
        this.bought = bought;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Product product))
            return false;

        if (visits != product.visits)
            return false;
        if (bought != product.bought)
            return false;
        return reference.equals(product.reference);
    }

    @Override
    public int hashCode() {
        int result = reference.hashCode();
        result = 31 * result + (int) (visits ^ (visits >>> 32));
        result = 31 * result + (int) (bought ^ (bought >>> 32));
        return result;
    }
}
