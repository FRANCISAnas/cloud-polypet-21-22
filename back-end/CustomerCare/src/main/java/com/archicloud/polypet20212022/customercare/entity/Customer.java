package com.archicloud.polypet20212022.customercare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class Customer {

    private String name;

    private String surname;

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Embedded
    private Cart cart;
}
