package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
@Getter
@Setter
public class Cart {

    @ElementCollection
    private List<OrderLine> orderLines;

}
