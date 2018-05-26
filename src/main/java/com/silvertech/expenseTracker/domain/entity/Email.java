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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email extends EntityDef{

    @javax.validation.constraints.Email
    private String emailAddress;
    @Column(name = DatabaseConstants.IS_PRIMARY)
    private boolean primary;

}
