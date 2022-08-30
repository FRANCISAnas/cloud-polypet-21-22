package com.archicloud.polypet20212022.customercare.services;

import com.archicloud.polypet20212022.customercare.entity.*;
import com.archicloud.polypet20212022.customercare.repositories.CustomerRepository;
import com.archicloud.polypet20212022.customercare.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentWS {

    @Value("${bank.server.url}")
    private String bankUrl;

    @Value("${inventory.repository.url}")
    private String inventoryUrl;

    @Value("${polypet.iban}")
    private String iban;

    @Value("${polypet.swiftcode}")
    private String swiftcode;

    private BankAccount bankAccount;

    @Autowired
    private CustomerServiceWS customerService;

    @Autowired
    private CustomerRepository customersRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentWS.class.getName());

    private final RestTemplate restTemplate;

    @Autowired
    public PaymentWS(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public PaymentResult payAndValid(String token, CreditCard creditCard, Address destination) throws Exception {

        logger.info("Payment ... ");
        Optional<Customer> optionalCustomer = customersRepository.findByEmail(token.split("_")[0]);

        if (optionalCustomer.isPresent()) {

            Customer customer = optionalCustomer.get();

            BigDecimal amount = getCustomerTTC(customer);

            var responseCartValidation = restTemplate.postForEntity(inventoryUrl + "/cart/book",
                    customer.getCart().getOrderLines(), boolean.class);

            if (responseCartValidation.getStatusCode() == HttpStatus.OK
                    && Boolean.TRUE.equals(responseCartValidation.getBody())) {
                var paymentRequest = new PaymentRequest(creditCard, this.bankAccount, amount);

                var response = restTemplate.postForEntity(bankUrl, paymentRequest, String.class);

                if (response.getStatusCode() == HttpStatus.OK) {

                    if (customerService.deliver(customer.getCart(), destination)) {

                        var responseDecrementsBoughtProducts = restTemplate.postForEntity(
                                inventoryUrl + "/cart/validate", customer.getCart().getOrderLines(), boolean.class);

                        if (responseDecrementsBoughtProducts.getStatusCode() == HttpStatus.OK
                                && Boolean.TRUE.equals(responseDecrementsBoughtProducts.getBody())) {
                            logger.info("Inventory updated succefully!");
                        } else {
                            logger.warn("Oops something went wrong while updating the inventory!");
                        }
                        customer.setCart(null);

                        customersRepository.save(customer);

                        logger.info("products delivered successfully.");

                        return PaymentResult.valueOf(response.getBody());

                    } else {
                        logger.warn("Sorry the deliver can't be done, you will be reimbursed.");
                        // TODO REIMBOURSE THE CLIENT
                        return PaymentResult.SUCCESS;
                    }

                }
            }

            logger.info("Sorry the cart can't be validate by the inventory! Please retry later.");
            return PaymentResult.FAILURE;
        }

        else {
            throw new Exception("Customer doesn't exist !");
        }

    }

    private BigDecimal getCustomerTTC(Customer customer) {
        double val = 0;

        for (OrderLine orderLine : customer.getCart().getOrderLines()) {
            if (orderLine.getProduct() == null || orderLine.getProduct().getPrice() == 0)
                continue;
            val += orderLine.getProduct().getPrice() * orderLine.getQuantity();
        }

        return new BigDecimal(val);
    }

    /**
     * PaymentRequest
     */
    public record PaymentRequest(CreditCard creditCard, BankAccount polypetAccount, BigDecimal amount) {
    }

    public record BankAccount(String iban, String swiftCode) {
    }

    public record ValidationInfo(CreditCard creditCard, Address destination) {

    }

}
