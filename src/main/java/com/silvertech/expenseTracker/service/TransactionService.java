package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.Transaction;
import com.silvertech.expenseTracker.domain.entity.TransactionLog;
import com.silvertech.expenseTracker.domain.entity.TransactionProjection;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.SomeThingWentWrongException;
import com.silvertech.expenseTracker.repository.AccountRepository;
import com.silvertech.expenseTracker.repository.TransactionLogRepository;
import com.silvertech.expenseTracker.repository.TransactionRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionLogRepository transactionLogRepository;

    public Transaction getTransactionById(UUID transactionId) {
        log.info("Find Transaction by Id {} " + transactionId);
        Transaction transaction = transactionRepository.getOne(transactionId);
        log.info("getTransactionById {} " + transaction);
        if (null == transaction) {
            log.error("Transaction not Found");
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.CATEGORY_NOT_FOUND);
        }
        log.info("Output getTransactionById {} ", transactionId);
        return transaction;
    }

    public Page<TransactionProjection> getTransactions(Pageable pageable) {
        log.info("Find Transaction All Transactions");
        return transactionRepository
                .findAllProjectedBy(pageable);
    }


    public List<Transaction> saveAllTransaction(List<Transaction> transactions) {
        List<Transaction> transactions1;
        log.info("Saving All Transaction");
        try {
            transactions1 = transactionRepository.saveAll(transactions);
            transactions.forEach(transaction -> {
                logTransaction(transaction);
                updateAccountBalance(transaction);
             });
        } catch (Exception e) {
            log.error("Error Occurred {} ", e.getMessage());
            throw new SomeThingWentWrongException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return transactions1;
    }

    private void updateAccountBalance(Transaction transaction) {
        log.info("Updating Account Balance {} ",transaction);
        Account beneficiaryAccount = transaction.getBeneficiaryAccount();
        Account debitAccount = transaction.getDebitAccount();
        beneficiaryAccount.setBalance(beneficiaryAccount.getBalance() + transaction.getAmount());
        log.info("updating account balance : beneficiaryAccount : {}", beneficiaryAccount);
        debitAccount.setBalance(debitAccount.getBalance() - transaction.getAmount());
        log.info("updating account balance : debitAccount : {}", debitAccount);
        accountRepository.saveAll(Arrays.asList(beneficiaryAccount, debitAccount));
    }

    public TransactionProjection findOneById(UUID transactionId){
        return transactionRepository.getById(transactionId);
    }

    public Page<TransactionProjection> getTransaction(Pageable pageable) {
        return transactionRepository.findAllProjectedBy(pageable);
    }

    public Page<TransactionProjection> getByAccountId(UUID uuid, Pageable pageable) {
        return transactionRepository.findAllByBeneficiaryAccountId(uuid, pageable);
    }

    public Page<TransactionProjection> getByCategoryId(String guid, Pageable pageable) {
        return transactionRepository.findAllByCategoryId(guid, pageable);
    }

    public Page<TransactionProjection> findAll(Specification<Transaction> spec,
                                               Class<TransactionProjection> transactionProjectionClass, Pageable pageable) {
        return transactionRepository.findAll(spec, transactionProjectionClass,pageable);
    }

    public Transaction updateTransaction(Transaction transaction, Transaction reverseTransaction) {

        log.info("Updating Transaction : {} ",transaction);
        try {
            Transaction tr = transactionRepository.save(transaction);
            reverseTransaction(reverseTransaction);
            updateAccountBalance(transaction);
            logTransaction(transaction);
            return tr;
        }catch (Exception e){
            log.error("Error Occurred {} ", e.getMessage());
            throw new SomeThingWentWrongException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void reverseTransaction(Transaction reverseTransaction){
        log.info("Reversing Transaction : {} ", reverseTransaction);
        Account reverseBeneficiary = reverseTransaction.getBeneficiaryAccount();
        Account reverseDabit = reverseTransaction.getDebitAccount();
        reverseBeneficiary.setBalance(reverseBeneficiary.getBalance()-reverseTransaction.getAmount());
        reverseDabit.setBalance(reverseDabit.getBalance()+reverseTransaction.getAmount());
        accountRepository.saveAll(Arrays.asList(reverseBeneficiary,reverseDabit));
    }

    private void logTransaction(Transaction transaction){
        TransactionLog transactionLog = constructTransaction(transaction);
        System.out.println(transactionLog);
        log.info("Logging transaction {} : ",transactionLog);
        transactionLogRepository.save(transactionLog);
    }

    private TransactionLog constructTransaction(Transaction transaction) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setAmount(transaction.getAmount());
        transactionLog.setBeneficiaryAccount(transaction.getBeneficiaryAccount().getId().toString());
        transactionLog.setDebitAccount(transaction.getDebitAccount().getId().toString());
        transactionLog.setCategory(transaction.getCategory().getId().toString());
        transactionLog.setDescription(transaction.getDescription());
        transactionLog.setDate(transaction.getDate());
        transactionLog.setLastModifiedDttm(transaction.getLastModifiedDttm());
        transactionLog.setLastModifiedUser(transaction.getLastModifiedUser());
        transactionLog.setCreatedDttm(transaction.getCreatedDttm());
        return transactionLog;
    }
    /*public List<Transaction> findAll(Specification<Transaction> spec) {
        return transactionRepository.findAll(spec);
    }*/
}
