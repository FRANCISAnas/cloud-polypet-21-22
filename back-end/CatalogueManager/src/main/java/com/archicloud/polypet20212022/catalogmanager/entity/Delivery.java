package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderPackage orderPackage;

    @Embedded
    private Address address;

}
