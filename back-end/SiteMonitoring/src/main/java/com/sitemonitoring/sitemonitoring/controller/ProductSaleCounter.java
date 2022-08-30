package com.sitemonitoring.sitemonitoring.controller;

import java.util.List;

import javax.validation.Valid;

import com.sitemonitoring.sitemonitoring.service.ProductSaleCounterWS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductSaleCounter {

    @Autowired
    private ProductSaleCounterWS productSaleCounterWS;

    @PostMapping("/update/sells")
    public void updateSells(@RequestBody @Valid ProductListDTO products) {
        productSaleCounterWS.updateSelling(products.products);
    }

    public record ProductListDTO(List<ProductDTO> products) {

    }

    public record ProductDTO(String reference) {

    }

}
