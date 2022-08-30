package com.archicloud.polypet20212022.customercare.services;

import com.archicloud.polypet20212022.customercare.entity.Cart;
import com.archicloud.polypet20212022.customercare.entity.Customer;
import com.archicloud.polypet20212022.customercare.entity.OrderLine;
import com.archicloud.polypet20212022.customercare.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManageCustomerCartWS {

    @Autowired
    private CustomerRepository customersRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentWS.class.getName());

    public OrderLine add(String token, OrderLine orderLine) throws Exception {
        logger.info("Adding ..." + orderLine);
        Optional<Customer> customerOptional = customersRepository.findByEmail(token.split("_")[0]);

        if (customerOptional.isPresent()) {

            Customer customer = customerOptional.get();

            Cart customerCart = customer.getCart();

            customerCart.add(orderLine);

            customer.setCart(customerCart);

            customersRepository.save(customer);

            logger.info("Product has been added successfully !");

            return orderLine;

        } else {
            throw new Exception("customer doesn't exist !");
        }

    }

    public String delete(String token, String productName) throws Exception {

        logger.info("Deleting products name : " + productName + " from cart ...");
        Optional<Customer> customerOptional = customersRepository.findByEmail(token.split("_")[0]);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            OrderLine orderLine = null;

            for (OrderLine ol : customer.getCart().getOrderLines()) {
                if (ol.getProduct().getName().equals(productName)) {
                    orderLine = ol;
                }
            }

            Cart customerCart = customer.getCart();

            customerCart.delete(orderLine);

            customer.setCart(customerCart);

            customersRepository.save(customer);

            return "Product deleted successfully !";

        }

        else {
            throw new Exception("customer doesn't exist !");
        }

    }

}
