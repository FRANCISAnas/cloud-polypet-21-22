package com.archicloud.polypet20212022.catalogmanager.services;

import com.archicloud.polypet20212022.catalogmanager.entity.InternalProduct;
import com.archicloud.polypet20212022.catalogmanager.entity.Partner;
import com.archicloud.polypet20212022.catalogmanager.entity.PartnerProduct;
import com.archicloud.polypet20212022.catalogmanager.repositories.InternalProductRepository;
import com.archicloud.polypet20212022.catalogmanager.repositories.PartnerProductRepository;
import com.archicloud.polypet20212022.catalogmanager.repositories.PartnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartnerSubscriberWs {
    private static final Logger log = LoggerFactory.getLogger(PartnerSubscriberWs.class.getName());

    PartnerRepository partnerRepository;

    @Autowired
    public PartnerSubscriberWs(InternalProductRepository internalProductRepository,
            PartnerProductRepository partnerProductRepository, PartnerRepository partnerRepository,
            RestTemplateBuilder restTemplateBuilder) {
        this.partnerRepository = partnerRepository;
    }

    public Boolean subscribePartner(String partnerName, String inventoryUrl) {
        try {
            log.info(String.format("Subscribing to partner %s by %s...", partnerName,
                    String.format("%s/j/subscribe", inventoryUrl)));
            var savedPartner = partnerRepository.save(new Partner(partnerName, inventoryUrl));
            log.info(String.format("Subscribed to partner %s with success!", partnerName));
            return true;
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}