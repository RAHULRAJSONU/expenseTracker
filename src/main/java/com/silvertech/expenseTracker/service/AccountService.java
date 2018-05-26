package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.resource.account.AccountCreateResponseBuilder;
import com.silvertech.expenseTracker.mapper.AccountCreateMapper;
import com.silvertech.expenseTracker.repository.AccountRepository;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class AccountService {
    AccountRepository accountRepository;
    CustomerRepository customerRepository;
    AccountCommonService accountCommonService;
    private AccountCreateMapper accountCreateMapper;
    private AccountCreateResponseBuilder accountCreateResponseBuilder;


    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> saveAllAccount(List<Account> accountList) {
        return accountRepository.saveAll(accountList);
    }
}
