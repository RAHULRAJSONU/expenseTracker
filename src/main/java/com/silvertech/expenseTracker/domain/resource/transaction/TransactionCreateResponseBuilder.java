package com.silvertech.expenseTracker.domain.resource.transaction;


import com.silvertech.expenseTracker.domain.entity.Transaction;
import com.silvertech.expenseTracker.domain.response.transaction.TransactionResponse;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class TransactionCreateResponseBuilder {

    public TransactionResponse createTransactionResponse(Transaction transaction) {
        TransactionResponse transactionResponse = TransactionResponse.builder()
                ._id(transaction.getId())
                .amount(Double.toString(transaction.getAmount()))
                .transactionType(transaction.getTransactionType())
                .build();
        Link selfLink = linkTo(Transaction.class)
                .slash("transaction")
                .slash(transactionResponse.get_id())
                .slash("?projection=transactionProjection")
                .withSelfRel();

        transactionResponse.add(selfLink);
        return transactionResponse;
    }

}
