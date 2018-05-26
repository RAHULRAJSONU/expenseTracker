package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.repository.AccountRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class AccountCommonService {

    AccountRepository accountRepository;

    public Account getAccountById(UUID accountId) {
        log.info("Find Account by Id " + accountId);
        Account account = accountRepository.getOne(accountId);
        log.info("getAccountById " + account);
        if (null == account) {
            log.error("Account not Found");
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.ACCOUNT_NOT_FOUND);
        }
        log.info("Output getAccountById {} ", accountId);
        return account;
    }

}
