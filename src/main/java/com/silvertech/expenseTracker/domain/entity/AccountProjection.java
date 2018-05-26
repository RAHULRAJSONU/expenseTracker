package com.silvertech.expenseTracker.domain.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "accountProjections", types = Account.class)
public interface AccountProjection {
    UUID getId();

    @Value("#{null!=target.getCustomer()?target.getCustomer().getFullName():null}")
    String getCustomer();

    String getCode();

    String getDescription();

    double getBalance();

    boolean isActive();
}
