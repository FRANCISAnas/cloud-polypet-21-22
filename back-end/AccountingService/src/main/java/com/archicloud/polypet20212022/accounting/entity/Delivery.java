package com.archicloud.polypet20212022.accounting.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    private Address destination;

    @Embedded
    private OrderPackage orderPackage;

    public Delivery() {
    }

    public Delivery(OrderPackage orderPackage, Address destination) {
        this.destination = destination;
        this.orderPackage = orderPackage;
    }
}
