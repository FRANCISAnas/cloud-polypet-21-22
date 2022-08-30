package com.archicloud.polypet20212022.customercare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
public class Cart {

    @ElementCollection(fetch = FetchType.EAGER)
    private List<OrderLine> orderLines;

    public void add(OrderLine orderLine) {
        if (orderLines == null) {
            orderLines = new ArrayList<>();
        }
        orderLines.add(orderLine);
    }

    public void delete(OrderLine orderLine) {
        if (orderLines != null && orderLine != null) {
            orderLines.remove(orderLine);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (OrderLine orderLine : orderLines)
            stringBuilder.append(orderLine.toString());
        return "Cart{" + "orderLines=[" + stringBuilder + "]}";
    }
}
