package com.silvertech.expenseTracker.domain.entity;

import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "mobileProjection", types = Mobile.class)
public interface MobileProjection {
    UUID getId();

    String getCountryCode();

    String getMobileNumber();

    boolean isPrimary();
}
