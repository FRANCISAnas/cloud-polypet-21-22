package com.archicloud.polypet20212022.accounting.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Embeddable
@Getter
@Setter
public class OrderPackage {

    private Long orderId;

    @ElementCollection
    private List<OrderLine> orderLines;

}
