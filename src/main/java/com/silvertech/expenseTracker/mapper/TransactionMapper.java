package com.silvertech.expenseTracker.mapper;

import com.silvertech.expenseTracker.domain.request.transaction.TransactionCreateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUICreateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUIUpdateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUpdateRequest;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionMapper {


    public TransactionUICreateRequest createTransactionCreateRequest
            (TransactionCreateRequest transactionCreateRequest, String userId) {
        TransactionUICreateRequest transactionUICreateRequest = new TransactionUICreateRequest();

        transactionUICreateRequest.setBeneficiaryAccountId(UUID.fromString(transactionCreateRequest.getBeneficiaryAccount()));
        transactionUICreateRequest.setDebitAccountId(UUID.fromString(transactionCreateRequest.getDebitAccount()));
        transactionUICreateRequest.setCategoryId(UUID.fromString(transactionCreateRequest.getCategory()));
        transactionUICreateRequest.setAmount(Double.parseDouble(transactionCreateRequest.getAmount()));
        transactionUICreateRequest.setTransactionType(transactionCreateRequest.getTransactionType());
        transactionUICreateRequest.setDate((new DateFormatUtil()
                .parseDate(transactionCreateRequest.getDate())).getTime());
        transactionUICreateRequest.setDescription(transactionCreateRequest.getDescription());
        transactionUICreateRequest.setUser(userId);

        return transactionUICreateRequest;
    }

    public TransactionUIUpdateRequest buildTransactionUpdateRequest(
            TransactionUpdateRequest transactionUpdateRequest, String userId) {
        TransactionUIUpdateRequest transactionUIUpdateRequest = new TransactionUIUpdateRequest();

        transactionUIUpdateRequest.setId(UUID.fromString(transactionUpdateRequest.getId()));
        transactionUIUpdateRequest.setDescription(transactionUpdateRequest.getDescription());
        transactionUIUpdateRequest.setAmount(Double.parseDouble(transactionUpdateRequest.getAmount()));
        transactionUIUpdateRequest.setBeneficiaryAccountId(UUID.fromString(transactionUpdateRequest.getBeneficiaryAccount()));
        transactionUIUpdateRequest.setDebitAccountId(UUID.fromString(transactionUpdateRequest.getDebitAccount()));
        transactionUIUpdateRequest.setTransactionType(transactionUpdateRequest.getTransactionType());
        transactionUIUpdateRequest.setCategoryId(UUID.fromString(transactionUpdateRequest.getCategory()));
        transactionUIUpdateRequest.setDate(new DateFormatUtil()
                .parseDate(transactionUpdateRequest.getDate()));
        transactionUIUpdateRequest.setUser(userId);

        return transactionUIUpdateRequest;
    }
}
