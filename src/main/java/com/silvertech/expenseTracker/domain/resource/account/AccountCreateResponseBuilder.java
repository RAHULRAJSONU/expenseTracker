package com.silvertech.expenseTracker.domain.resource.account;


import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.response.account.AccountResponse;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class AccountCreateResponseBuilder {

    public AccountResponse createAccountResponse(Account account) {
        AccountResponse accountResponse = AccountResponse.builder()
                ._id(account.getId())
                .parentAccount(getParent(account))
                .build();
        Link selfLink = linkTo(Account.class)
                .slash("accounts")
                .slash(accountResponse.get_id())
                .slash("?projection=accountProjection")
                .withSelfRel();

        accountResponse.add(selfLink);
        return accountResponse;
    }

    private String getParent(Account account) {
        return null != account.getParentAccount() ? account.getParentAccount().getId().toString() : null;
    }

}
