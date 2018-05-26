package com.silvertech.expenseTracker.domain.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.silvertech.expenseTracker.annotation.ValidDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionUpdateRequest {

    @NotNull(message = "TransactionId cannot be null.")
    @JsonProperty("id")
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "Transaction id should match pattern:" +
                    "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}.")
    private String id;

    @NotNull(message = "amount should not be null.")
    @Pattern(regexp = "^[-+]?[0-9]+(,[0-9]{3})*(\\.[0-9]+)?([eE][-+]?[0-9]+)?",
            message = "amount should match ^[-+]?[0-9]+(,[0-9]{3})*(\\.[0-9]+)?([eE][-+]?[0-9]+)? .")
    @JsonProperty("amount")
    private String amount;

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "beneficiaryAccountId should match ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$.")
    @JsonProperty("beneficiaryAccountId")
    private String beneficiaryAccount;

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "debitAccountId should match ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$.")
    @JsonProperty("debitAccountId")
    private String debitAccount;

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "CategoryId should match ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$.")
    @JsonProperty("categoryId")
    private String category;

    @NotNull(message = "Description should not be null.")
    @Size(max = 50, message = "description should not be more than 50 characters.")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "Transaction date should not be null")
    @ValidDate(message = "Date should be valid")
    private String date;

    @NotNull(message = "Transaction type should not be null")
    @Pattern(regexp = "^(?i)INCOME|(?i)EXPENSE|(?i)LOAN", message = "Transaction type is not valid.")
    private String transactionType;

}
