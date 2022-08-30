package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Consultation {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String country;

    private LocalDateTime date;

}
