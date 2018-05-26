package com.silvertech.expenseTracker.domain.entity;

import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "genderProjection", types = {Gender.class})
public interface GenderProjection {
    UUID getId();

    String getSex();
}
