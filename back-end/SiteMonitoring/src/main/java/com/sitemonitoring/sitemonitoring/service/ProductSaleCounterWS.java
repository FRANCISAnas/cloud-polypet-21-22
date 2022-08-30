package com.sitemonitoring.sitemonitoring.service;

import com.sitemonitoring.sitemonitoring.controller.ProductSaleCounter;
import com.sitemonitoring.sitemonitoring.entity.Product;
import com.sitemonitoring.sitemonitoring.repository.SalesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleCounterWS {

    private static final Logger log = LoggerFactory.getLogger(ProductSaleCounterWS.class.getName());

    @Autowired
    private SalesRepository salesRepository;

    public void updateSelling(List<ProductSaleCounter.ProductDTO> productsDTO) {

        for (ProductSaleCounter.ProductDTO p : productsDTO) {
            Optional<Product> productOptional = salesRepository.findProductByReference(p.reference());

            log.info("updating selling of product id : " + p.reference());

            if (productOptional.isPresent()) {

                Product product = productOptional.get();

                product.setBought(product.getBought() + 1);

                salesRepository.save(product);
            } else {
                Product product = new Product();

                product.setReference(p.reference());
                product.setVisits(1);
                product.setBought(1);

                salesRepository.save(product);

            }

        }

    }

}
