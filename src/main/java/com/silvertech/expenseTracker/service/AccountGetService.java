package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.AccountProjection;
import com.silvertech.expenseTracker.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountGetService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AccountRepository accountRepository;

    public Page<AccountProjection> getAll(Pageable pageable) {
        return accountRepository.findAllProjectedBy(pageable);
    }

    public Page<AccountProjection> getByParent(Account parent, Pageable pageable) {
        return accountRepository.findByParentAccount(parent, pageable);
    }
}
