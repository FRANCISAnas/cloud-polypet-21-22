package com.archicloud.polypet20212022.customercare.services;

import com.archicloud.polypet20212022.customercare.entity.*;
import com.archicloud.polypet20212022.customercare.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@EnableAsync
public class SellingInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SellingInterceptor.class.getName());

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${monitor.site.url}")
    private String monitorUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public SellingInterceptor(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public void incrementSell(String token) {

        log.info("Intercepting");

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(token.split("_")[0]);

        if (optionalCustomer.isPresent()) {

            Customer customer = optionalCustomer.get();

            List<OrderLine> orderLines = new ArrayList<>(customer.getCart().getOrderLines());

            System.out.println("orderLines = " + orderLines);

            List<ProductDTO> boughtProducts = new ArrayList<>();

            for (OrderLine orderLine : orderLines) {
                boughtProducts.add(new ProductDTO(orderLine.getProduct().getReference()));
            }

            var responseEntity = restTemplate.postForEntity(monitorUrl + "/update/sells",
                    new ProductListDTO(boughtProducts), ProductListDTO.class);

            if (responseEntity.getStatusCode().isError()) {
                log.info("Something went wrong while updating consulting counter :-/");
            }

        }
    }

    public record ProductListDTO(List<ProductDTO> products) {

    }

    public record ProductDTO(String reference) {

    }

}
