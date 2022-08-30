package com.sitemonitoring.sitemonitoring.service;

import com.sitemonitoring.sitemonitoring.entity.Product;
import com.sitemonitoring.sitemonitoring.repository.SalesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductConsultSaleWS {

    @Autowired
    private SalesRepository salesRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductConsultSaleWS.class.getName());

    public void updateConsulting(String reference) {

        log.info("updating consulting of product id : " + reference);

        Optional<Product> productOptional = salesRepository.findProductByReference(reference);

        if (productOptional.isPresent()) {

            Product product = productOptional.get();

            product.setVisits(product.getVisits() + 1);

            salesRepository.save(product);
        } else {

            Product p = new Product();

            p.setVisits(1);
            p.setBought(1);
            p.setReference(reference);

            salesRepository.save(p);
        }

    }

}
