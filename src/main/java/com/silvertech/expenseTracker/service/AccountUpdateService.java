package com.silvertech.expenseTracker.service;


import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.request.account.AccountUIUpdateRequest;
import com.silvertech.expenseTracker.domain.request.account.AccountUpdateRequest;
import com.silvertech.expenseTracker.domain.request.account.AccountUpdateRequestList;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.repository.AccountRepository;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import com.silvertech.expenseTracker.validator.AccountValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class AccountUpdateService {

    private AccountValidator accountValidator;
    private AccountService accountService;
    private ApplicationEventPublisher applicationEventPublisher;
    private AccountCommonService accountCommonService;
    private AccountRepository accountRepository;

    @Transactional
    public CreateUpdateResponse updateAccount
            (List<AccountUpdateRequestList> accountUpdateRequestLists, String userId) {
        List<UUID> updateAccountIds = new ArrayList<>();
        log.info("account update request {} "
                , accountUpdateRequestLists);
        for (AccountUpdateRequestList accountUpdateRequestList : accountUpdateRequestLists) {

            for (AccountUpdateRequest accountUpdateRequest
                    : accountUpdateRequestList.getAccountUpdateRequests()) {
                accountValidator.validateAccountUpdateRequest(accountUpdateRequest);

                UUID updateAccountId = updateAccount(buildAccountUpdateRequest(accountUpdateRequest, userId));

                updateAccountIds.add(updateAccountId);
            }
        }
        log.info("Output updateCategory HTTP Status code {} ",
                HttpStatus.OK.value());
        /*applicationEventPublisher.publishEvent(new AfterSaveEvent(CategoryUpdateEvent.builder()
                .updateCategoryIds(updateCategoryIds).build()));*/
        return new CreateUpdateResponse(HttpStatus.OK.value());
    }

    public UUID updateAccount(AccountUIUpdateRequest accountUIUpdateRequest) {

        log.info("UpdateAccount request received{} ", accountUIUpdateRequest);
        UUID accountId = accountUIUpdateRequest.getId();
        Account account = accountCommonService.getAccountById(accountId);

        if (validateAccountUpdate(account, accountUIUpdateRequest)) {
            log.info(" validateAccountUpdate returned true:No Updates " +
                    "found for Account table");
            return account.getId();
        }

        if (!validateAccountDescriptionUpdate(account,
                accountUIUpdateRequest)) {
            account.setDescription(accountUIUpdateRequest
                    .getDescription()
                    .toUpperCase());
        }

        if (!validateAccountActiveUpdate
                (account, accountUIUpdateRequest)) {
            account.setActive(accountUIUpdateRequest.getActive());
        }
        if (accountUIUpdateRequest.getUser() != null) {
            account.setLastModifiedUser(accountUIUpdateRequest.getUser());
        }
        account.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());

        Account updatedAccount = accountService.save(account);

        log.info("Output updateAccount Id {} ", updatedAccount.getId());
        return updatedAccount.getId();
    }

    private boolean validateAccountUpdate(Account account,
                                          AccountUIUpdateRequest accountUIUpdateRequest) {
        boolean noDescriptionUpdate = validateAccountDescriptionUpdate(account,
                accountUIUpdateRequest);

        boolean noActiveUpdate = validateAccountActiveUpdate
                (account, accountUIUpdateRequest);

        if (noDescriptionUpdate && noActiveUpdate) {
            return true;
        }
        return false;

    }

    private boolean validateAccountActiveUpdate
            (Account account,
             AccountUIUpdateRequest accountUIUpdateRequest) {
        boolean noActiveUpdate = false;
        if (accountUIUpdateRequest.getActive() == null) {
            noActiveUpdate = true;
        } else if (account.isActive() == accountUIUpdateRequest.getActive()) {
            noActiveUpdate = true;

        }


        return noActiveUpdate;
    }

    private boolean validateAccountDescriptionUpdate(Account account,
                                                     AccountUIUpdateRequest
                                                             accountUIUpdateRequest) {
        boolean noDescriptionUpdate = false;
        if (accountUIUpdateRequest.getDescription() == null) {
            noDescriptionUpdate = true;
        } else if (account.getDescription()
                .equalsIgnoreCase(accountUIUpdateRequest.getDescription())) {
            noDescriptionUpdate = true;

        }

        return noDescriptionUpdate;
    }

    private AccountUIUpdateRequest buildAccountUpdateRequest(
            AccountUpdateRequest accountUpdateRequest, String userId) {

        AccountUIUpdateRequest accountUIUpdateRequest = new AccountUIUpdateRequest();
        accountUIUpdateRequest.setId(UUID.fromString(accountUpdateRequest.getId()));
        accountUIUpdateRequest.setActive(accountUpdateRequest.getActive());
        accountUIUpdateRequest.setDescription(accountUpdateRequest.getDescription());
        accountUIUpdateRequest.setUser(userId);
        return accountUIUpdateRequest;
    }
}
