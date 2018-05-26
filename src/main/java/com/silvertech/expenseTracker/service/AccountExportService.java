package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.AccountProjection;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.repository.AccountRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class AccountExportService {

    AccountRepository accountRepository;

    AccountCommonService accountCommonService;

    public Page<AccountProjection> getSubAccount(UUID accountId, Pageable pageable) {
        Account account = accountCommonService.getAccountById(accountId);
        if (account == null) {
            log.error("Parent Account not found for accountId: " + accountId);
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.ACCOUNT_NOT_FOUND + accountId);
        } else {
            return accountRepository
                    .findByParentAccount(account, pageable);
        }
    }

    public Page<AccountProjection> getAccounts(Pageable pageable) {
        return accountRepository
                .findAllProjectedBy(pageable);
    }

    public Optional<Account> getAccountById(UUID guid) {
        return accountRepository.findById(guid);
    }
}
