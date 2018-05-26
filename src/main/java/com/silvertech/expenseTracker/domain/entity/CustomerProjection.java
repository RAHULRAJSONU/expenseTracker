package com.silvertech.expenseTracker.domain.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.UUID;

@Projection(name = "customerProjection", types = {Customer.class})
public interface CustomerProjection {
    UUID getId();

    String getFirstName();

    String getMiddleName();

    String getLastName();

    long getDob();

    @Value("#{target.getGender().getSex()}")
    String getGender();

    List<AddressProjection> getAddress();

    List<EmailProjection> getEmail();

    List<MobileProjection> getMobile();
}
