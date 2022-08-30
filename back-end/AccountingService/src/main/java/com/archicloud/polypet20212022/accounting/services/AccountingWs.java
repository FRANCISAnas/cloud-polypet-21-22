package com.archicloud.polypet20212022.accounting.services;

import com.archicloud.polypet20212022.accounting.entity.Address;
import com.archicloud.polypet20212022.accounting.entity.Delivery;
import com.archicloud.polypet20212022.accounting.entity.OrderPackage;
import com.archicloud.polypet20212022.accounting.repositories.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountingWs {
    private static final Logger log = LoggerFactory.getLogger(AccountingWs.class.getName());

    @Autowired
    DeliveryRepository deliveryRepository;

    public Boolean updateTurnover(OrderPackage order, Address destination) {
        var savedDelivery = deliveryRepository.save(new Delivery(order, destination));
        log.info(String.format("Order %s effectively saved", order.getOrderId()));
        log.info(String.format("Destination %s", savedDelivery.getDestination()));
        try {
            savedDelivery.getOrderPackage().getOrderLines().forEach(orderLine -> {
                var product = orderLine.getProduct();
                log.info(String.format("Product: reference %s, name %s, price %s", product.getReference(),
                        product.getName(), product.getPrice().toString()));
                log.info("From: " + product.getOrigin());
                log.info("Stored at: " + product.getAddress());
                log.info(String.format("Quantity bought: %s.", orderLine.getQuantity()));
            });
        } catch (Exception e) {
            // e.printStackTrace();
            log.info("Oops something went wrong with orderlines, they were not sent.");
        }

        return true;
    }

    public record DeliveryDTO(OrderPackage order, Address destination) {

    }
}