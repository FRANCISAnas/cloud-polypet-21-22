package com.archicloud.polypet20212022.customercare.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
public class Address {

    private String roadName;

    private Integer buildingNumber;

    private String zipCode;

    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Address address))
            return false;
        return Objects.equals(getRoadName(), address.getRoadName())
                && Objects.equals(getBuildingNumber(), address.getBuildingNumber())
                && Objects.equals(getZipCode(), address.getZipCode())
                && Objects.equals(getCountry(), address.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoadName(), getBuildingNumber(), getZipCode(), getCountry());
    }
}
