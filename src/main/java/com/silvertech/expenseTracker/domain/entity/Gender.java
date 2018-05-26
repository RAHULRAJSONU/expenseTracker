package com.silvertech.expenseTracker.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@javax.persistence.Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Gender extends EntityDef{

    private String sex;

}
