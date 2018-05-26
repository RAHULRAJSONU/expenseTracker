package com.silvertech.expenseTracker.domain.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionUIUpdateRequest {

    @NotNull
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("beneficiaryAccountId")
    private UUID beneficiaryAccountId;

    @JsonProperty("debitAccountId")
    private UUID debitAccountId;

    @JsonProperty("categoryId")
    private UUID categoryId;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("user")
    private String user;

}
