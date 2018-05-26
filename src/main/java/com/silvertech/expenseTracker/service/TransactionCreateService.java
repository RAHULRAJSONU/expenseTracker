package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Transaction;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionCreateRequest;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionCreateRequestList;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUICreateRequest;
import com.silvertech.expenseTracker.domain.resource.transaction.TransactionCreateResponseBuilder;
import com.silvertech.expenseTracker.domain.response.transaction.TransactionCreateResponse;
import com.silvertech.expenseTracker.domain.response.transaction.TransactionResponse;
import com.silvertech.expenseTracker.domain.response.transaction.TransactionResponseList;
import com.silvertech.expenseTracker.mapper.TransactionMapper;
import com.silvertech.expenseTracker.repository.TransactionRepository;
import com.silvertech.expenseTracker.validator.TransactionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TransactionCreateService {

    List<TransactionResponse> transactionResponses;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionValidator transactionValidator;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private CategoryCommonService categoryCommonService;
    @Autowired
    private AccountCommonService accountCommonService;
    @Autowired
    private TransactionCreateResponseBuilder transactionCreateResponseBuilder;

    @Transactional
    public TransactionCreateResponse createTransaction
            (List<TransactionCreateRequestList> transactionCreateRequestLists, String userId) {
        List<TransactionResponseList> transactionResponseLists = new ArrayList<>();
        log.info("Input createTransaction {} ", transactionCreateRequestLists);
        if(transactionValidator.validateTransactionRequest(transactionCreateRequestLists)){
            for (TransactionCreateRequestList transactionCreateRequestList : transactionCreateRequestLists) {
                TransactionResponseList transactionResponseList = new TransactionResponseList();
                transactionResponses = new ArrayList<TransactionResponse>();
                log.info("Create Request for Transaction " + transactionCreateRequestList);
                createNewTransaction(transactionCreateRequestList, userId);
                transactionResponseList.setTransactionResponses(transactionResponses);
                transactionResponseLists.add(transactionResponseList);
            }
        }
        log.info("Output createTransaction HTTP Response {} ", HttpStatus.CREATED.value());
        /*applicationEventPublisher.publishEvent(new AfterCreateEvent(CategoryCreateEvent.builder()
                .categoryCreateResponse(genderResponseLists).build()));*/
        return new TransactionCreateResponse(HttpStatus.CREATED.value(), transactionResponseLists);
    }

    private void createNewTransaction(TransactionCreateRequestList transactionCreateRequestList,
                                      String userId) {

        List<Transaction> transactionList = new ArrayList<>();

        List<Transaction> responseTransactionList;
        for (TransactionCreateRequest transactionCreateRequest
                : transactionCreateRequestList.getTransactionCreateRequests()) {
            Transaction transaction = callCreateTransactionService(
                    transactionCreateRequest, userId);
            transactionList.add(transaction);
        }
        responseTransactionList = transactionService.saveAllTransaction(transactionList);
        buildTransactionResponses(responseTransactionList, transactionResponses);
    }

    public Transaction callCreateTransactionService(TransactionCreateRequest transactionCreateRequest,
                                                    String userId) {
        log.info("Input callCreateTransactionService {} ", transactionCreateRequest);
        Transaction transaction
                = createTransaction(transactionMapper.createTransactionCreateRequest
                (transactionCreateRequest, userId));
        log.info("Output callCreateTransactionService {} ", transaction);
        return transaction;
    }

    private Transaction createTransaction(TransactionUICreateRequest transactionUICreateRequest) {

        log.info("Transaction create request {}", transactionUICreateRequest);
        Transaction transaction = constructTransaction(transactionUICreateRequest);
        return transaction;
    }

    private Transaction constructTransaction(TransactionUICreateRequest transactionUICreateRequest) {
         Transaction transaction = new Transaction();
                 transaction.setBeneficiaryAccount(accountCommonService.getAccountById(transactionUICreateRequest
                        .getBeneficiaryAccountId()));
        transaction.setDebitAccount(accountCommonService.getAccountById(transactionUICreateRequest.getDebitAccountId()));
        transaction.setCategory(categoryCommonService.getCategoryById(transactionUICreateRequest.getCategoryId()));
        transaction.setAmount(transactionUICreateRequest.getAmount());
        transaction.setDate(transactionUICreateRequest.getDate());
        transaction.setDescription(transactionUICreateRequest.getDescription());
        transaction.setTransactionType(transactionUICreateRequest.getTransactionType());
        transaction.setCreatedDttm(new Date());
        transaction.setLastModifiedDttm(new Date());
        transaction.setLastModifiedUser(transactionUICreateRequest.getUser());
        return transaction;
    }

    public Boolean buildTransactionResponses(List<Transaction> transactions,
                                             List<TransactionResponse> transactionResponses) {
        Boolean transactionResponse;
        for (Transaction transaction : transactions) {
            transactionResponses.add(transactionCreateResponseBuilder
                    .createTransactionResponse(transaction));
        }
        transactionResponse = true;
        return transactionResponse;
    }

}
