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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@javax.persistence.Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends EntityDef{

    private String firstName;
    private String middleName;
    private String lastName;

    private long dob;
    @OneToOne(cascade = CascadeType.ALL)
    private Gender gender;

    @JoinColumn(name = "customer_id")
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Email> email;

    @JoinColumn(name = "customer_id")
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Address> address;

    @JoinColumn(name = "customer_id")
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Mobile> mobile;

    public String getFullName() {
        if (null != this.middleName) {
            return this.firstName + " " + this.middleName + " " + this.lastName;
        } else {
            return this.firstName + " " + this.lastName;
        }
    }
}
