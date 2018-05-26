package com.silvertech.expenseTracker.domain.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountUICreateRequest {

    @JsonProperty("parentId")
    private UUID parentAccount;

    @JsonProperty("customerId")
    private UUID customer;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("user")
    private String user;

}
