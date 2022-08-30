package com.example.delivery.controller;

import com.example.delivery.entity.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DeliveryManager {

    @PostMapping("/deliver")
    public ResponseEntity<String> deliver(@RequestBody @Valid Cart cart) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
