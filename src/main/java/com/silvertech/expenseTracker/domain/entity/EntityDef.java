package com.silvertech.expenseTracker.domain.entity;

import com.silvertech.expenseTracker.domain.entity.constant.DatabaseConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class EntityDef implements Serializable {

    private static final long serialVersionUID = -6470090944414208496L;

    @Id
    @GeneratedValue(generator = "uuid")
    @Access(AccessType.PROPERTY)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(columnDefinition = "VARCHAR(36)", name = DatabaseConstants.ID)
    private UUID id;

    @DiffIgnore
    @Column(name = DatabaseConstants.CREATED_DTTM, columnDefinition = "timestamp")
    private Date createdDttm;

    @DiffIgnore
    @Column(name = DatabaseConstants.LAST_MODIFIED_DTTM, columnDefinition = "timestamp")
    private Date lastModifiedDttm;

    @DiffIgnore
    @Column(name = DatabaseConstants.LAST_MODIFIED_USER)
    private String lastModifiedUser;

    @DiffIgnore
    @Version
    private Long version;
}
