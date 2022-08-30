package com.archicloud.polypet20212022.accounting.controllers;

import com.archicloud.polypet20212022.accounting.services.AccountingWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountingController {
    private static final Logger log = LoggerFactory.getLogger(AccountingController.class.getName());

    @Autowired
    AccountingWs accountingWs;

    @PostMapping("/delivery/save")
    private Boolean findProductByReference(@RequestBody AccountingWs.DeliveryDTO deliveryDTO) {
        try {
            return accountingWs.updateTurnover(deliveryDTO.order(), deliveryDTO.destination());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
