package com.silvertech.expenseTracker.domain.resource.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.silvertech.expenseTracker.domain.entity.Address;
import com.silvertech.expenseTracker.domain.entity.Email;
import com.silvertech.expenseTracker.domain.entity.Gender;
import com.silvertech.expenseTracker.domain.entity.Mobile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonRootName("categories")
public class UICustomerProjectionResource extends ResourceSupport {
    @JsonProperty("id")
    private UUID _id;
    private String firstName;
    private String middleName;
    private String lastName;
    private long dob;
    private Gender gender;
    private List<Address> addressList;
    private List<Email> emailList;
    private List<Mobile> mobileList;
    private Date createDateTime;
    private Date lastModifiedDateTime;
    private String lastModifiedUser;
}
