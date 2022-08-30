package com.archicloud.polypet20212022.customercare.controllers;

import com.archicloud.polypet20212022.customercare.entity.PaymentResult;
import com.archicloud.polypet20212022.customercare.services.PaymentWS;
import com.archicloud.polypet20212022.customercare.services.SellingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class Payment {

    @Autowired
    private SellingInterceptor sellingInterceptor;

    @Autowired
    private PaymentWS paymentWS;


    @PostMapping("/confirm/{token}")
    public PaymentResult payAndValid(@PathVariable("token") String token, @RequestBody @Valid PaymentWS.ValidationInfo validationInfo) {
        try{
            PaymentResult result = paymentWS.payAndValid(token, validationInfo.creditCard(),validationInfo.destination());
            if (result.equals(PaymentResult.SUCCESS)){
                sellingInterceptor.incrementSell(token);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return PaymentResult.FAILURE;
        }
    }


}
