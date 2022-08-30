package com.archicloud.polypet20212022.catalogmanager.controllers;

import com.archicloud.polypet20212022.catalogmanager.services.PartnerSubscriberWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PartnerSubscriberController {

    private static final Logger log = LoggerFactory.getLogger(PartnerSubscriberController.class.getName());

    @Autowired
    private PartnerSubscriberWs partnerSubscriberWs;

    @PostMapping("/partner/{name}/subscribe")
    private Boolean findProductByReference(@PathVariable("name") String partnerName, @RequestBody String inventoryUrl) {
        try {
            return partnerSubscriberWs.subscribePartner(partnerName, inventoryUrl);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
