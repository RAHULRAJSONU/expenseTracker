package com.silvertech.expenseTracker.domain.entity;

import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "addressProjection", types = Address.class)
public interface AddressProjection {
    UUID getId();

    String getCountry();

    String getState();

    String getCity();

    String getStreetAddress();

    String getZip();

    String getAddressType();

}
