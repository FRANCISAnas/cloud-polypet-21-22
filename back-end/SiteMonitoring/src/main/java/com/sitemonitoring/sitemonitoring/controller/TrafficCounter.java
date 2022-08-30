package com.sitemonitoring.sitemonitoring.controller;

import com.sitemonitoring.sitemonitoring.entity.Product;
import com.sitemonitoring.sitemonitoring.service.TrafficCounterWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrafficCounter {

    @Autowired
    private TrafficCounterWS trafficCounterWS;

    @GetMapping("/consult/{reference}")
    public Product productConsultStats(@PathVariable("reference") String ref) throws Exception {
        return trafficCounterWS.getProductStatistic(ref);
    }

}
