package com.silvertech.expenseTracker.domain.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountCreateRequestList {
    @Valid
    @NotNull(message = "AccountCreateRequests should not be null.")
    private List<AccountCreateRequest> accountCreateRequests;
}
