package com.silvertech.expenseTracker.domain.entity;

import com.silvertech.expenseTracker.annotation.Sequence;
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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends EntityDef{

    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;
    @Sequence
    private String code;
    private String description;
    private double balance;
    private boolean active;
    @ManyToOne
    private Account parentAccount;

}
