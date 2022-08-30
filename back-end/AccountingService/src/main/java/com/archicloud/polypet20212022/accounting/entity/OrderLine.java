package com.archicloud.polypet20212022.accounting.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter
@Setter
public class OrderLine {

    @Embedded
    private Product product;

    private Integer quantity;

}
