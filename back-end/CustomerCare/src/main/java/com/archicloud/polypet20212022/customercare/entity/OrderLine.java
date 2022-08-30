package com.archicloud.polypet20212022.customercare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter
@Setter
public class OrderLine {

    @Embedded
    private Product product;

    private Integer quantity;

    @Override
    public String toString() {
        return "OrderLine{" + "product=" + product + ", quantity=" + quantity + '}';
    }
}
