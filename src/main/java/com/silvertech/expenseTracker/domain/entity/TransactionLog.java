package com.silvertech.expenseTracker.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class TransactionLog extends EntityDef{

    private long date;
    @Column(name = "beneficiary_account_id")
    private String beneficiaryAccount;
    @Column(name = "debit_account_id")
    private String debitAccount;
    @Column(name = "category_id")
    private String category;
    private double amount;
    private String description;

}
