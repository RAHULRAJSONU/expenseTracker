package com.silvertech.expenseTracker.domain.entity;

import com.silvertech.expenseTracker.domain.entity.constant.DatabaseConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@javax.persistence.Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends EntityDef{

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Account beneficiaryAccount;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Account debitAccount;
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
    @Column(name = "ammount")
    private double amount;
    private long date;
    private String description;
    private String transactionType;

}
