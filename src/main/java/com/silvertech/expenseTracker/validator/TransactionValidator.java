package com.silvertech.expenseTracker.validator;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.entity.TransactionType;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionCreateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionCreateRequestList;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUpdateRequest;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorCodeValidationException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.repository.TransactionRepository;
import com.silvertech.expenseTracker.service.AccountCommonService;
import com.silvertech.expenseTracker.service.CategoryCommonService;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class TransactionValidator {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountCommonService accountService;
    @Autowired
    private CategoryCommonService categoryService;

    public void validateTransactionUpdateRequest(
            TransactionUpdateRequest transactionUpdateRequest) {
        if (null == transactionUpdateRequest.getId() &&
                null == transactionUpdateRequest.getBeneficiaryAccount() &&
                null == transactionUpdateRequest.getDebitAccount() &&
                null == transactionUpdateRequest.getAmount() &&
                null == transactionUpdateRequest.getDate() &&
                null == transactionUpdateRequest.getCategory() &&
                null == transactionUpdateRequest.getDescription() &&
                null == transactionUpdateRequest.getTransactionType()) {
            throw new ErrorCodeException(HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_UPDATE);
        }
    }

    public boolean validateTransactionRequest(
            List<TransactionCreateRequestList> transactionCreateRequestLists) {
        if (CollectionUtils.isEmpty(transactionCreateRequestLists)) {
            throw new ErrorCodeException
                    (HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_CREATE);
        } else {
            return validateTransactionCreateRequest(transactionCreateRequestLists);
        }
    }

    private boolean validateTransactionCreateRequest(
            List<TransactionCreateRequestList> transactionCreateRequestLists) {
        Set<ErrorMessage> errorMessageSet = new HashSet();
        for (TransactionCreateRequestList transactionCreateRequestList :
                transactionCreateRequestLists) {
            validateTransactionRequestList(transactionCreateRequestList, errorMessageSet);
        }

        if (errorMessageSet.isEmpty()) {
            return true;
        } else {
            List<ErrorMessage> errorMessageList = new ArrayList<>();
            errorMessageList.addAll(errorMessageSet);
            throw new ErrorCodeValidationException(HttpStatus.BAD_REQUEST, errorMessageList);
        }
    }

    private void validateTransactionRequestList(TransactionCreateRequestList transactionCreateRequestList,
                                                Set<ErrorMessage> errorMessageSet) {
        for (TransactionCreateRequest transactionCreateRequest
                : transactionCreateRequestList.getTransactionCreateRequests()) {
            validateAccount(transactionCreateRequest, errorMessageSet);
            validateCategory(transactionCreateRequest, errorMessageSet);
            validateTransactionType(transactionCreateRequest, errorMessageSet);
            validateAmount(transactionCreateRequest, errorMessageSet);
        }
    }

    private void validateCategory(TransactionCreateRequest transactionCreateRequest, Set<ErrorMessage> errorMessageSet) {
        Category category = categoryService.getCategoryById(UUID.fromString(transactionCreateRequest.getCategory()));
        log.info("Validating for Category ", category);
        if (null == category) {
            log.error(Constants.BAD_REQUEST + "," + Constants.CATEGORY_NOT_FOUND + ":"
                    + transactionCreateRequest.getCategory());
            errorMessageSet.add(new ErrorMessage(Constants.CATEGORY_NOT_FOUND,
                    transactionCreateRequest.getCategory()));
        }
    }

    private void validateAmount(TransactionCreateRequest transactionCreateRequest, Set<ErrorMessage> errorMessageSet) {
        try {
            double amt = parseAmount(transactionCreateRequest);
            if (0 > amt) {
                log.error(Constants.BAD_REQUEST + "," + Constants.CAN_NOT_ACCEPT_NEGATIVE_AMMOUNT + ":"
                        + transactionCreateRequest.getAmount());
            }
        } catch (Exception e) {
            log.error(Constants.BAD_REQUEST + "," + Constants.AMMOUNT_NOT_VALID + ":"
                    + transactionCreateRequest.getAmount());
            errorMessageSet.add(new ErrorMessage(Constants.AMMOUNT_NOT_VALID,
                    transactionCreateRequest.getAmount()));
        }
    }

    private double parseAmount(TransactionCreateRequest transactionCreateRequest) {
        return Double.parseDouble(transactionCreateRequest.getAmount());
    }

    private void validateTransactionType(TransactionCreateRequest transactionCreateRequest, Set<ErrorMessage> errorMessageSet) {
        Map<String, TransactionType> enumMap = EnumUtils.getEnumMap(TransactionType.class);
        TransactionType type = enumMap.get(transactionCreateRequest.getTransactionType());
        log.info("Validating for Transaction Type ", transactionCreateRequest.getTransactionType());
        if (null == type) {
            log.error(Constants.BAD_REQUEST + "," + Constants.TRANSACTION_TYPE_NOT_FOUND + ":"
                    + transactionCreateRequest.getTransactionType());
            errorMessageSet.add(new ErrorMessage(Constants.TRANSACTION_TYPE_NOT_FOUND,
                    transactionCreateRequest.getTransactionType()));
        }
    }

    private void validateAccount(TransactionCreateRequest transactionCreateRequest, Set<ErrorMessage> errorMessageSet) {
        Account beneficiaryAccount = accountService.getAccountById(UUID.fromString(transactionCreateRequest.getBeneficiaryAccount()));
        Account debitAccount = accountService.getAccountById(UUID.fromString(transactionCreateRequest.getDebitAccount()));
        log.info("Validating for beneficiaryAccount & debitAccount {} {}", beneficiaryAccount, debitAccount);
        if (null == beneficiaryAccount) {
            log.error(Constants.BAD_REQUEST + "," + Constants.ACCOUNT_NOT_FOUND + ":"
                    + transactionCreateRequest.getBeneficiaryAccount());
            errorMessageSet.add(new ErrorMessage(Constants.ACCOUNT_NOT_FOUND,
                    transactionCreateRequest.getBeneficiaryAccount()));
        } else if (null == debitAccount) {
            log.error(Constants.BAD_REQUEST + "," + Constants.ACCOUNT_NOT_FOUND + ":"
                    + transactionCreateRequest.getDebitAccount());
            errorMessageSet.add(new ErrorMessage(Constants.ACCOUNT_NOT_FOUND,
                    transactionCreateRequest.getDebitAccount()));
        } else if (beneficiaryAccount.equals(debitAccount)) {
            log.error(Constants.BAD_REQUEST + "," + Constants.DEBIT_AND_BENEFICIARY_ACCOUNT_CAN_NOT_BE_SAME + "- Debit Account:"
                    + transactionCreateRequest.getDebitAccount() + "; -Beneficiary Account: " +
                    transactionCreateRequest.getBeneficiaryAccount());
            errorMessageSet.add(new ErrorMessage(Constants.DEBIT_AND_BENEFICIARY_ACCOUNT_CAN_NOT_BE_SAME,
                    Arrays.asList(transactionCreateRequest.getDebitAccount(), transactionCreateRequest.getBeneficiaryAccount())));
        } else {
            complyAmountDeduction(debitAccount, errorMessageSet, parseAmount(transactionCreateRequest));
        }

    }

    private void complyAmountDeduction(Account debitAccount, Set<ErrorMessage> errorMessageSet, double amount) {
        if (debitAccount.getBalance() < amount) {
            log.error(Constants.BAD_REQUEST + "," + Constants.INSUFFICIENT_FUND + "- Debit Account:"
                    + debitAccount + " : Transaction Amount: " + amount);
            errorMessageSet.add(new ErrorMessage(Constants.INSUFFICIENT_FUND,
                    debitAccount.getCode()));
        }
    }

}
