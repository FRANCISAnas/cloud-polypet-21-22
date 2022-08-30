package com.archicloud.polypet20212022.customercare.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class PackagesOrder {
    @Id
    @Column(name = "trackingNumber", nullable = false)
    private String trackingNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<OrderLine> orderLines;

    public PackagesOrder() {

    }

    public PackagesOrder(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        if (this.orderLines == null)
            this.orderLines = new ArrayList<>();
        this.orderLines.addAll(orderLines);
    }
}
