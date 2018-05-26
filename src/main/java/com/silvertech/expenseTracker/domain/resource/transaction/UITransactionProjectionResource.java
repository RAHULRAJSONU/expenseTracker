package com.silvertech.expenseTracker.domain.resource.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.Category;
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
@JsonRootName("transactions")
public class UITransactionProjectionResource extends ResourceSupport {
    @JsonProperty("id")
    private UUID _id;
    private double amount;
    private String transactionType;
    private Category category;
    private Account beneficiaryAccount;
    private Account debitAccount;
    private String description;
    private Date createDateTime;
    private Date lastModifiedDateTime;
    private String lastModifiedUser;
}
