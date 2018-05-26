package com.silvertech.expenseTracker.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.UUID;

@Projection(name = "categoryProjection", types = Category.class)
public interface CategoryProjection {

    UUID getId();

    String getName();

    String getDescription();

    @Value("#{target.getParent()==null ? null : target.getParent().getId()}")
    UUID getParent();

    @JsonProperty(value = "active")
    boolean isActive();

    Date getCreatedDttm();

    Date getLastModifiedDttm();

    String getLastModifiedUser();

}
