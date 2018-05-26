package com.silvertech.expenseTracker.domain.resource.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.silvertech.expenseTracker.domain.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonRootName("accounts")
public class UIAccountProjectionResource extends ResourceSupport {
    @JsonProperty("id")
    private UUID _id;
    private String code;
    private String description;
    private Account parentAccount;
    @JsonProperty("active")
    private boolean active;
    private Date createDateTime;
    private Date lastModifiedDateTime;
    private String lastModifiedUser;
}
