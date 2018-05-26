package com.silvertech.expenseTracker.domain.entity;

import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "emailProjection", types = Email.class)
public interface EmailProjection {
    UUID getId();

    String getEmailAddress();

    boolean isPrimary();
}
