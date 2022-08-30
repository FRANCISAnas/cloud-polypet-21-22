package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Partner {

    private String inventoryUrl;
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    public Partner(String name, String inventoryUrl) {
        this.name = name;
        this.inventoryUrl = inventoryUrl;
    }

    public Partner() {

    }
}
