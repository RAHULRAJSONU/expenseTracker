package com.silvertech.expenseTracker.domain.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.UUID;

@Projection(name = "transactionProjection", types = Transaction.class)
public interface TransactionProjection {
    UUID getId();

    double getAmount();

    @Value("#{target.getDebitAccount().getCode()}")
    String getDebitAccount();

    @Value("#{target.getBeneficiaryAccount().getCode()}")
    String getBeneficiaryAccount();

    @Value("#{target.getCategory().getId().toString()}")
    String getCategory();

    @Value("#{target.getCategory().getName()}")
    String getCategoryName();

    long getDate();

    String getDescription();

    String getTransactionType();

}
