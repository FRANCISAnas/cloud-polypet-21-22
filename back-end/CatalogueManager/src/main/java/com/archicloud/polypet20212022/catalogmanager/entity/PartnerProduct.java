package com.archicloud.polypet20212022.catalogmanager.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
public class PartnerProduct extends Product {
    private static final Logger log = LoggerFactory.getLogger(PartnerProduct.class.getName());

    @PrePersist
    private void prePersistPartnerProduct() {
        log.info("PrePersist method called");
        if (reference == null || reference.length() == 0) {
            generateReference();
        }
    }
}
