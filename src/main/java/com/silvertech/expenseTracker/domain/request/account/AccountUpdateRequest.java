package com.silvertech.expenseTracker.domain.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AccountUpdateRequest {

    @NotNull(message = "AccountId cannot be null.")
    @JsonProperty("id")
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "Account id should match pattern:" +
                    "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}.")
    private String id;

    @NotNull(message = "Description should not be null.")
    @Size(max = 50, message = "description should not be more than 50 characters.")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "active field should not be null.")
    @JsonProperty("Active")
    private Boolean active;

}
