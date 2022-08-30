package com.archicloud.polypet20212022.customercare.controllers;

import com.archicloud.polypet20212022.customercare.entity.Customer;
import com.archicloud.polypet20212022.customercare.entity.Product;
import com.archicloud.polypet20212022.customercare.services.CustomerServiceWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CustomerService {

    @Autowired
    private CustomerServiceWS customerServiceWS;

    @GetMapping("/products/{name}")
    public List<Product> findProductInCatalogueByName(@PathVariable("name") String name) throws Exception {
        return customerServiceWS.findProductInCatalogueByName(name);
    }

    @GetMapping("/products/category/{category}")
    public List<Product> findProductInCatalogueByCategory(@PathVariable("category") String category) throws Exception {
        return customerServiceWS.findProductInCatalogueByCategory(category);
    }

    @PostMapping("/customer")
    public String subscribe(@RequestBody @Valid Customer customer) {
        return customerServiceWS.subscribe(customer);
    }

    @GetMapping("/customer/{name}")
    public String login(@RequestBody @Valid String email) {
        return customerServiceWS.log(email);
    }

}
