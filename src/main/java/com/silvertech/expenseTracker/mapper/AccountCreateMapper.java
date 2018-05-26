package com.silvertech.expenseTracker.mapper;

import com.silvertech.expenseTracker.domain.request.account.AccountCreateRequest;
import com.silvertech.expenseTracker.domain.request.account.AccountUICreateRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountCreateMapper {


    public AccountUICreateRequest createAccountCreateRequest
            (AccountCreateRequest accountCreateRequest, String userId) {
        AccountUICreateRequest accountUICreateRequest = new AccountUICreateRequest();
        accountUICreateRequest.setCustomer(UUID.fromString(accountCreateRequest.getCustomer()));
        accountUICreateRequest.setActive(accountCreateRequest.getActive());
        accountUICreateRequest.setDescription(accountCreateRequest.getDescription());
        if (null != accountCreateRequest.getParentAccount()) {
            accountUICreateRequest.setParentAccount(UUID.fromString(accountCreateRequest.getParentAccount()));
        }
        accountUICreateRequest.setUser(userId);
        return accountUICreateRequest;
    }
}
