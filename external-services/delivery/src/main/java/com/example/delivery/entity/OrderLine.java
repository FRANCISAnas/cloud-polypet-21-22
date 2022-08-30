package com.example.delivery.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
@Getter
@Setter
public class OrderLine {

    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    private Integer quantity;

}
