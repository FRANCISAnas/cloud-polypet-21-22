package com.archicloud.polypet20212022.catalogmanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class CreditCard {

    private String cardNumber;

    private String ccv;

    private String expirationDate;

}
