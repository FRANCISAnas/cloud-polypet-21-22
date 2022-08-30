package com.example.delivery.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Cart {

    @ElementCollection
    private List<OrderLine> orderLines;

}
