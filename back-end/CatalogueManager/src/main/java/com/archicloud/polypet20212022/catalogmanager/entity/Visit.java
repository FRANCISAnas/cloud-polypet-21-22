package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Visit {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String connectionOrigin;

    private String country;

    private boolean connected;

    private String page;

}
