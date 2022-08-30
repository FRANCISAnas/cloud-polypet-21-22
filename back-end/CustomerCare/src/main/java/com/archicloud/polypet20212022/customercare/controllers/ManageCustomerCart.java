package com.archicloud.polypet20212022.customercare.controllers;

import com.archicloud.polypet20212022.customercare.entity.OrderLine;
import com.archicloud.polypet20212022.customercare.services.ManageCustomerCartWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ManageCustomerCart {

    @Autowired
    private ManageCustomerCartWS manageCustomerCartWS;

    @PostMapping("/add/{token}")
    public OrderLine add(@PathVariable("token") String token, @RequestBody @Valid OrderLine orderLine)
            throws Exception {
        return manageCustomerCartWS.add(token, orderLine);
    }

    @GetMapping("delete/{token}/{productName}")
    public String delete(@PathVariable("token") String token, @PathVariable("productName") String productName)
            throws Exception {
        return manageCustomerCartWS.delete(token, productName);
    }

}
