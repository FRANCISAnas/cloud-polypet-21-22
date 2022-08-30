package com.archicloud.polypet20212022.catalogmanager.entity;

import javax.persistence.Entity;

@Entity
public class InternalProduct extends Product {
    public static final String JolyPetLocalName = "JolyPet";

    public InternalProduct() {
        super();
        setOrigin(JolyPetLocalName);
    }

}
