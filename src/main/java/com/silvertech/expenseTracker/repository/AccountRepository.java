package com.silvertech.expenseTracker.repository;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.AccountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Page<AccountProjection> findAllProjectedBy(Pageable pageable);

    Page<AccountProjection> findByParentAccount(Account parentAccount, Pageable pageable);

    Account findByCustomerId(UUID customer);
}
