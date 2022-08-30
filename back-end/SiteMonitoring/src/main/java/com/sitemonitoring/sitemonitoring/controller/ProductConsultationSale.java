package com.sitemonitoring.sitemonitoring.controller;

import com.sitemonitoring.sitemonitoring.service.ProductConsultSaleWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductConsultationSale {

    @Autowired
    private ProductConsultSaleWS productConsultSaleWS;

    @GetMapping("/update/consulting/{reference}")
    public void updateConsulting(@PathVariable("reference") String reference) {
        productConsultSaleWS.updateConsulting(reference);
    }
}
