package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.request.account.AccountCreateRequest;
import com.silvertech.expenseTracker.domain.request.account.AccountCreateRequestList;
import com.silvertech.expenseTracker.domain.request.account.AccountUICreateRequest;
import com.silvertech.expenseTracker.domain.resource.account.AccountCreateResponseBuilder;
import com.silvertech.expenseTracker.domain.response.account.AccountCreateResponse;
import com.silvertech.expenseTracker.domain.response.account.AccountResponse;
import com.silvertech.expenseTracker.domain.response.account.AccountResponseList;
import com.silvertech.expenseTracker.mapper.AccountCreateMapper;
import com.silvertech.expenseTracker.repository.AccountRepository;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import com.silvertech.expenseTracker.validator.AccountValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AccountCreateService {

    private List<AccountResponse> accountResponses;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountValidator accountValidator;
    @Autowired
    private AccountCreateMapper accountCreateMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountCommonService accountCommonService;
    @Autowired
    private AccountCreateResponseBuilder accountCreateResponseBuilder;

    @Transactional
    public AccountCreateResponse createAccount
            (List<AccountCreateRequestList> accountCreateRequestLists, String userId) {
        List<AccountResponseList> accountResponseLists = new ArrayList<>();
        log.info("Input createAccount {} ", accountCreateRequestLists);
        Map<String, Account> accountHashMap = accountValidator.
                validateAccountRequest(accountCreateRequestLists);
        for (AccountCreateRequestList accountCreateRequestList : accountCreateRequestLists) {
            AccountResponseList accountResponseList = new AccountResponseList();
            accountResponses = new ArrayList<AccountResponse>();
            log.info("Create Request for Account " + accountCreateRequestList);
            createNewAccount(accountCreateRequestList, accountHashMap, userId);
            accountResponseList.setAccountResponses(accountResponses);
            accountResponseLists.add(accountResponseList);
        }
        log.info("Output createAccount HTTP Response {} ", HttpStatus.CREATED.value());
        /*applicationEventPublisher.publishEvent(new AfterCreateEvent(CategoryCreateEvent.builder()
                .categoryCreateResponse(genderResponseLists).build()));*/

        return new AccountCreateResponse(HttpStatus.CREATED.value(), accountResponseLists);
    }

    private void createNewAccount(AccountCreateRequestList accountCreateRequestList,
                                  Map<String, Account> accountMap, String userId) {

        List<Account> accountList = new ArrayList<>();

        List<Account> responseAccountList;
        for (AccountCreateRequest accountCreateRequest
                : accountCreateRequestList.getAccountCreateRequests()) {
            Account parentAccount = accountMap.get(accountCreateRequest.getParentAccount());
            Account account = callCreateAccountService(
                    accountCreateRequest, parentAccount, userId);
            accountList.add(account);
        }
        responseAccountList = accountService.saveAllAccount(accountList);
        buildAccountResponses(responseAccountList, accountResponses);
    }

    private Account callCreateAccountService(AccountCreateRequest accountCreateRequest,
                                             Account parentAccount,
                                             String userId) {
        log.info("Input callCreateAccountService {} ", accountCreateRequest);
        Account account
                = createAccount(accountCreateMapper.createAccountCreateRequest
                (accountCreateRequest, userId), parentAccount);
        log.info("Output callCreateAccountService {} ", account);
        return account;
    }

    private Account createAccount(AccountUICreateRequest accountUICreateRequest,
                                  Account parentAccount) {

        log.info("Account create request {}", accountUICreateRequest);
        Account account = constructAccount(parentAccount, accountUICreateRequest);
        return account;
    }

    public Boolean buildAccountResponses(List<Account> accounts,
                                         List<AccountResponse> accountResponses) {
        Boolean accountResponse;
        for (Account responseAccount : accounts) {
            accountResponses.add(accountCreateResponseBuilder
                    .createAccountResponse(responseAccount));
        }
        accountResponse = true;
        return accountResponse;
    }

    private Account constructAccount(Account parentAccount,
                                     AccountUICreateRequest accountUICreateRequest)

    {
        Account account = new Account();
        account.setCustomer(customerRepository.getById(accountUICreateRequest.getCustomer()));
        account.setDescription(accountUICreateRequest.getDescription().toUpperCase());
        account.setActive(accountUICreateRequest.getActive());
        account.setBalance(0.0);
        account.setParentAccount(parentAccount);
        account.setLastModifiedUser(accountUICreateRequest.getUser());
        account.setCreatedDttm(new DateFormatUtil().getCurrentPstDate());
        account.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());

        return account;
    }
}
