package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class OrderPackage {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ElementCollection
    private List<OrderLine> orderLines;

}
