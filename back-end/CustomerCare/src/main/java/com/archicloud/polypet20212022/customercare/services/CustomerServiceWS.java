package com.archicloud.polypet20212022.customercare.services;

import com.archicloud.polypet20212022.customercare.entity.*;
import com.archicloud.polypet20212022.customercare.repositories.CustomerRepository;
import com.archicloud.polypet20212022.customercare.repositories.OrderRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Service
public class CustomerServiceWS {

    private RestTemplate restTemplate;

    @Value("${inventory.repository.url}")
    private String inventoryUrl;

    @Value("${delivery.server.url}")
    private String deliveryUrl;

    @Value("${accounting.server.url}")
    private String accountingUrl;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customersRepository;

    private final Logger logger = Logger.getLogger(CustomerServiceWS.class.getName());

    @Autowired
    public CustomerServiceWS(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public List<Product> findProductInCatalogueByName(String name) throws Exception {
        logger.info("Sending request " + name);
        var response = this.restTemplate.getForEntity(inventoryUrl + "/search/" + name, ProductDTOs.class);
        return getProducts((ResponseEntity<ProductDTOs>) response);
    }

    public List<Product> findProductInCatalogueByCategory(String category) throws Exception {
        logger.info("Sending request to fetch category " + category);
        var response = this.restTemplate.getForEntity(inventoryUrl + "/search/category/" + category, ProductDTOs.class);
        return getProducts((ResponseEntity<ProductDTOs>) response);
    }

    private List<Product> getProducts(ResponseEntity<ProductDTOs> response) throws Exception {
        List<Product> products = new ArrayList<>();
        Objects.requireNonNull(response.getBody()).productDTOS().forEach(productDTO -> {
            productDTO.product().setAddress(productDTO.address());
            products.add(productDTO.product());
        });
        if (response.getStatusCode() == HttpStatus.OK) {
            return products;
        }
        throw new Exception("Request returned error, probably you entered an invalid name of a products");
    }

    public boolean deliver(Cart cart, Address destination) throws Exception {

        var order = new PackagesOrder(generatetrackingNumber());

        order.setOrderLines(cart.getOrderLines());

        logger.info(
                String.format("Sending request to deliver order %s to %s... ", order.getTrackingNumber(), destination));
        try {
            // var response= this.restTemplate.postForEntity(deliveryUrl+"/deliver", new DeliveryDTO(order,destination),
            // DeliveryDTO.class);
            var response = new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.warning("Request returned error, we can't deliver the package;");
                return false;
            } else {
                var responseAccounting = this.restTemplate.postForEntity(accountingUrl + "/delivery/save",
                        new DeliveryDTO(order, destination), boolean.class);
                return Boolean.TRUE.equals(responseAccounting.getBody());
            }
        } catch (RestClientException e) {
            // e.printStackTrace();
            return false;
        }

    }

    private String generatetrackingNumber() {
        String trackingNumber = RandomStringUtils.randomAlphabetic(3, 4).toUpperCase(Locale.ENGLISH) + "-"
                + RandomStringUtils.randomNumeric(5);
        while (this.orderRepository.existsBytrackingNumber(trackingNumber)) {
            trackingNumber = RandomStringUtils.randomAlphabetic(3, 4).toUpperCase(Locale.ENGLISH) + "-"
                    + RandomStringUtils.randomNumeric(5);
        }
        return trackingNumber;
    }

    private String generateToken(String email) {
        return email + "_" + UUID.randomUUID();
    }

    public String subscribe(Customer customer) {
        try {
            var customerOpt = customersRepository.findByEmail(customer.getEmail());
            return customerOpt.map(value -> generateToken(value.getEmail()))
                    .orElseGet(() -> generateToken(customersRepository.save(customer).getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String log(String email) {
        return generateToken(email);
    }

    public record Products(List<Product> products) {
    }

    public record ProductDTO(Product product, Address address) {

    }

    public record ProductDTOs(List<ProductDTO> productDTOS) {

    }

    public record DeliveryDTO(PackagesOrder order, Address destination) {

    }

}
