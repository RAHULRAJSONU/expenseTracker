package com.silvertech.expenseTracker.validator;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.request.account.AccountCreateRequest;
import com.silvertech.expenseTracker.domain.request.account.AccountCreateRequestList;
import com.silvertech.expenseTracker.domain.request.account.AccountUpdateRequest;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorCodeValidationException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.repository.AccountRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Component
@Slf4j
public class AccountValidator {
    @Autowired
    private AccountRepository accountRepository;

    public void validateAccountUpdateRequest(
            AccountUpdateRequest accountUpdateRequest) {
        if (accountUpdateRequest.getDescription() == null &&
                accountUpdateRequest.getActive() == null) {
            throw new ErrorCodeException(HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_UPDATE);
        }
    }

    public Map<String, Account> validateAccountRequest(
            List<AccountCreateRequestList> accountCreateRequestLists) {
        if (CollectionUtils.isEmpty(accountCreateRequestLists)) {
            throw new ErrorCodeException
                    (HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_CREATE);
        } else {
            return validateAccountCreateRequest(accountCreateRequestLists);
        }
    }

    private Map<String, Account> validateAccountCreateRequest(
            List<AccountCreateRequestList> accountCreateRequestLists) {
        Map<String, Account> accountHashMap = new HashMap<String, Account>();
        Set<ErrorMessage> errorMessageSet = new HashSet();
        for (AccountCreateRequestList accountCreateRequestList :
                accountCreateRequestLists) {
            validateAccountRequestList(accountCreateRequestList, errorMessageSet, accountHashMap);
        }

        if (errorMessageSet.isEmpty()) {
            return accountHashMap;
        } else {
            List<ErrorMessage> errorMessageList = new ArrayList<>();
            errorMessageList.addAll(errorMessageSet);
            throw new ErrorCodeValidationException(HttpStatus.BAD_REQUEST, errorMessageList);
        }
    }

    private void validateAccountRequestList(AccountCreateRequestList accountCreateRequestList,
                                            Set<ErrorMessage> errorMessageSet,
                                            Map<String, Account> accountHashMap) {
        for (AccountCreateRequest accountCreateRequest
                : accountCreateRequestList.getAccountCreateRequests()) {
            if (null != accountCreateRequest.getParentAccount()) {
                Account account = accountRepository.getOne(UUID.fromString(accountCreateRequest.getParentAccount()));
                log.info("getting Account for parent id- " + account);
                if (account == null) {
                    log.error(Constants.BAD_REQUEST + "," + Constants.PARENT_ACCOUNT_DOES_NOT_EXISTS + ":"
                            + accountCreateRequest.getParentAccount());
                    errorMessageSet.add(new ErrorMessage(Constants.PARENT_CATEGORY_DOES_NOT_EXISTS,
                            accountCreateRequest.getParentAccount()));
                } else {
                    accountHashMap.put(accountCreateRequest.getParentAccount(), account);
                    validateForUniqueAccount(
                            accountCreateRequest, errorMessageSet, accountHashMap);
                }
            } else {
                validateForUniqueAccount(
                        accountCreateRequest, errorMessageSet, accountHashMap);
            }
        }
    }

    private void validateForUniqueAccount(
            AccountCreateRequest accountCreateRequest, Set<ErrorMessage> errorMessageSet,
            Map<String, Account> accountMap) {
        Account account = accountRepository.findByCustomerId(UUID.fromString(accountCreateRequest.getCustomer()));
        if (account != null) {
            log.error(Constants.BAD_REQUEST + "," + Constants.RECORD_ALREADY_EXIST + ":"
                    + accountCreateRequest.getCustomer());
            errorMessageSet.add(new ErrorMessage(Constants.RECORD_ALREADY_EXIST,
                    accountCreateRequest.getCustomer()));
        } else {
            accountMap.put(accountCreateRequest.getCustomer(), account);
        }
    }

}
