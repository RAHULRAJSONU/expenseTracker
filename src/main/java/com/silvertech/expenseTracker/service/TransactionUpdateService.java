package com.silvertech.expenseTracker.service;


import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.entity.Transaction;
import com.silvertech.expenseTracker.domain.request.CategoryUIUpdateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUIUpdateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUpdateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUpdateRequestList;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.exception.ValidationException;
import com.silvertech.expenseTracker.mapper.TransactionMapper;
import com.silvertech.expenseTracker.repository.TransactionRepository;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import com.silvertech.expenseTracker.validator.TransactionValidator;
import com.silvertech.expenseTracker.validator.Validation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
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
public class TransactionUpdateService {

    private TransactionValidator transactionValidator;

    private TransactionService transactionService;

    private AccountCommonService accountCommonService;

    private CategoryCommonService categoryCommonService;

    private TransactionRepository transactionRepository;

    private TransactionMapper transactionMapper;

    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public CreateUpdateResponse updateTransaction
            (List<TransactionUpdateRequestList> transactionUpdateRequestLists, String userId) {
        List<UUID> updateTransactionIds = new ArrayList<>();
        log.info("transaction update request {} "
                , transactionUpdateRequestLists);
        for (TransactionUpdateRequestList transactionUpdateRequestList : transactionUpdateRequestLists) {

            for (TransactionUpdateRequest transactionUpdateRequest
                    : transactionUpdateRequestList.getTransactionUpdateRequests()) {
                transactionValidator.validateTransactionUpdateRequest(transactionUpdateRequest);

                UUID updateTransactionId = processUpdateTransaction(transactionMapper.
                        buildTransactionUpdateRequest(transactionUpdateRequest, userId));

                updateTransactionIds.add(updateTransactionId);
            }
        }
        log.info("Output updateTransaction HTTP Status code {} ",
                HttpStatus.OK.value());
        /*applicationEventPublisher.publishEvent(new AfterSaveEvent(CategoryUpdateEvent.builder()
                .updateCategoryIds(updateCategoryIds).build()));*/
        return new CreateUpdateResponse(HttpStatus.OK.value());
    }

    public UUID processUpdateTransaction(TransactionUIUpdateRequest transactionUIUpdateRequest) {

        log.info("UpdateTransaction request received{} ", transactionUIUpdateRequest);
        UUID transactionId = transactionUIUpdateRequest.getId();
        Transaction transaction = transactionService.getTransactionById(transactionId);
        Transaction reverseTransaction = (Transaction) SerializationUtils.clone(transaction);
        log.info("Old Transaction {} ", reverseTransaction);
        if (validateTransactionUpdate(transaction, transactionUIUpdateRequest)) {
            log.info(" No Updates found for this transaction : ",transactionId);
            return transaction.getId();
        }

        if (!validateTransactionDescriptionUpdate(transaction,
                transactionUIUpdateRequest)) {
            transaction.setDescription(transactionUIUpdateRequest
                    .getDescription()
                    .toUpperCase());
        }

        if (!validateTransactionAmountUpdate
                (transaction, transactionUIUpdateRequest)) {
            transaction.setAmount(transactionUIUpdateRequest.getAmount());
        }

        if (!validateTransactionBeneficiaryUpdate(transaction, transactionUIUpdateRequest)){
            transaction.setBeneficiaryAccount(accountCommonService.getAccountById(
                    transactionUIUpdateRequest.getBeneficiaryAccountId()));
        }
        if (!validateTransactionDateUpdate(transaction, transactionUIUpdateRequest)){
            transaction.setDate(transactionUIUpdateRequest.getDate().getTime());
        }
        if (!validateTransactionDebitAccountUpdate(transaction,transactionUIUpdateRequest)){
            transaction.setDebitAccount(accountCommonService.getAccountById(
                    transactionUIUpdateRequest.getDebitAccountId()));
        }
        if (!validateTransactionCategoryUpdate(transaction,transactionUIUpdateRequest)){
            transaction.setCategory(categoryCommonService.getCategoryById(transactionUIUpdateRequest.getCategoryId()));
        }
        if (!validateTransactionTypeUpdate(transaction,transactionUIUpdateRequest)){
            transaction.setTransactionType(transactionUIUpdateRequest.getTransactionType());
        }
        if (transactionUIUpdateRequest.getUser() != null) {
            transaction.setLastModifiedUser(transactionUIUpdateRequest.getUser());
        }
        transaction.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());

        Transaction updatedTransaction = transactionService.updateTransaction(transaction, reverseTransaction);

        log.info("Output updateTransaction Id {} ", updatedTransaction.getId());
        return updatedTransaction.getId();
    }

    private boolean validateTransactionUpdate(Transaction transaction,
                                              TransactionUIUpdateRequest transactionUIUpdateRequest) {
        boolean noDescriptionUpdate = validateTransactionDescriptionUpdate(transaction,
                transactionUIUpdateRequest);
        boolean noAmountUpdate = validateTransactionAmountUpdate
                (transaction, transactionUIUpdateRequest);
        boolean noBeneficiaryUpdate = validateTransactionBeneficiaryUpdate(transaction, transactionUIUpdateRequest);
        boolean noDebitAccountUpdate = validateTransactionDebitAccountUpdate(transaction, transactionUIUpdateRequest);
        boolean noTransactionDateUpdate = validateTransactionDateUpdate(transaction, transactionUIUpdateRequest);
        boolean noTransactionTypeUpdate = validateTransactionTypeUpdate(transaction, transactionUIUpdateRequest);
        boolean noTransactionCategoryUpdate = validateTransactionCategoryUpdate(transaction, transactionUIUpdateRequest);

        if (noDescriptionUpdate && noAmountUpdate && noBeneficiaryUpdate && noDebitAccountUpdate && noTransactionDateUpdate
                && noTransactionTypeUpdate && noTransactionCategoryUpdate) {
            return true;
        }
        return false;

    }

    private boolean validateTransactionBeneficiaryUpdate(Transaction transaction, TransactionUIUpdateRequest transactionUIUpdateRequest) {
        boolean noBeneficiaryUpdate = false;
        if (null == transactionUIUpdateRequest.getBeneficiaryAccountId()){
            noBeneficiaryUpdate = true;
        }else if (transaction.getBeneficiaryAccount().getId().equals(transactionUIUpdateRequest.getBeneficiaryAccountId())){
            noBeneficiaryUpdate = true;
        }
        return noBeneficiaryUpdate;
    }

    private boolean validateTransactionDebitAccountUpdate(Transaction transaction, TransactionUIUpdateRequest transactionUIUpdateRequest) {
        boolean noDabitAccountUpdate = false;
        if(null == transactionUIUpdateRequest.getDebitAccountId()){
            noDabitAccountUpdate = true;
        }else if (transaction.getDebitAccount().getId().equals(transactionUIUpdateRequest.getDebitAccountId())){
            noDabitAccountUpdate = true;
        }
        return noDabitAccountUpdate;
    }

    private boolean validateTransactionDateUpdate(Transaction transaction, TransactionUIUpdateRequest transactionUIUpdateRequest) {
        boolean noTransactionDateUpdate = false;
        if (null == transactionUIUpdateRequest.getDate()){
            noTransactionDateUpdate = true;
        }else if (transaction.getDate()== transactionUIUpdateRequest.getDate().getTime()){
            noTransactionDateUpdate = true;
        }
        return noTransactionDateUpdate;
    }

    private boolean validateTransactionTypeUpdate(Transaction transaction, TransactionUIUpdateRequest transactionUIUpdateRequest) {
        boolean noTransactionTypeUpdate = false;
        if(null == transactionUIUpdateRequest.getTransactionType()){
            noTransactionTypeUpdate = true;
        }else if (transaction.getTransactionType().equalsIgnoreCase(transactionUIUpdateRequest.getTransactionType())){
            noTransactionTypeUpdate = true;
        }
        return noTransactionTypeUpdate;
    }

    private boolean validateTransactionCategoryUpdate(Transaction transaction, TransactionUIUpdateRequest transactionUIUpdateRequest) {
        boolean noCategoryUpdate = false;
        if(null == transactionUIUpdateRequest.getCategoryId()){
            noCategoryUpdate = true;
        }else if (transactionUIUpdateRequest.getCategoryId().equals(transaction.getCategory().getId())){
            noCategoryUpdate = true;
        }

        return noCategoryUpdate;
    }

    private boolean validateTransactionAmountUpdate(Transaction transaction,
             TransactionUIUpdateRequest transactionUIUpdateRequest) {
        boolean noAmountUpdate = false;
        try {
            if (transactionUIUpdateRequest.getAmount() < 0) {
                noAmountUpdate = true;
            } else if (transaction.getAmount() == transactionUIUpdateRequest.getAmount()) {
                noAmountUpdate = true;
            }
            log.info("Validating transaction Amount update: found change : {}", noAmountUpdate);
            return noAmountUpdate;
        }catch (Exception e){
            throw new ValidationException("Amount is not valid");
        }

    }

    private boolean validateTransactionDescriptionUpdate(Transaction transaction,
                                                         TransactionUIUpdateRequest
                                                              transactionUIUpdateRequest) {
        boolean noDescriptionUpdate = false;
        if (transactionUIUpdateRequest.getDescription() == null) {
            noDescriptionUpdate = true;
        } else if (transaction.getDescription()
                .equalsIgnoreCase(transactionUIUpdateRequest.getDescription())) {
            noDescriptionUpdate = true;

        }

        return noDescriptionUpdate;
    }

}
