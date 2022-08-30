package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String surname;

    @Email
    private String email;

    @Embedded
    private Cart cart;

}
