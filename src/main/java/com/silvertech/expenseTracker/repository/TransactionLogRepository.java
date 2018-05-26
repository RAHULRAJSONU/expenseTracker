package com.silvertech.expenseTracker.repository;

import com.silvertech.expenseTracker.domain.entity.TransactionLog;
import com.silvertech.expenseTracker.utils.RepositoryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = RepositoryConstants.TRANSACTION_LOG_RESOURCE,
        path = RepositoryConstants.TRANSACTION_LOG_RESOURCE)
public interface TransactionLogRepository extends JpaRepository <TransactionLog , UUID> {

    @Override
    @RestResource
    TransactionLog save(TransactionLog transactionLog);
}
