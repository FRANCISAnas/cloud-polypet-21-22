package com.sitemonitoring.sitemonitoring.service;

import com.sitemonitoring.sitemonitoring.entity.Product;
import com.sitemonitoring.sitemonitoring.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrafficCounterWS {

    @Autowired
    private SalesRepository salesRepository;

    public Product getProductStatistic(String ref) throws Exception {

        Optional<Product> productOptional = salesRepository.findProductByReference(ref);

        if (productOptional.isPresent()) {

            return productOptional.get();

        } else {
            Product product = new Product();

            product.setReference(ref);
            product.setVisits(0);
            product.setBought(0);

            return salesRepository.save(product);

        }
    }

}
