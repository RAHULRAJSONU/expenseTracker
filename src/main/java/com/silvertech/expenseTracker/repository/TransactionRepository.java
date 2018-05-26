package com.silvertech.expenseTracker.repository;

import com.silvertech.expenseTracker.domain.entity.Transaction;
import com.silvertech.expenseTracker.domain.entity.TransactionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction>,
        JpaSpecificationExecutorWithProjection<Transaction> {
    Page<TransactionProjection> findAllProjectedBy(Pageable pageable);
    public TransactionProjection getById(UUID id);
    Page<TransactionProjection> findAllByBeneficiaryAccountId(UUID id, Pageable pageable);
    Page<TransactionProjection> findAllByCategoryId(String id, Pageable pageable);
}
