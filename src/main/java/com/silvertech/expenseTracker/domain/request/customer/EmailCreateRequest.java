package com.silvertech.expenseTracker.domain.request.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailCreateRequest {

    @NotNull(message = "email address should not be null.")
    @JsonProperty("emailAddress")
    @Size(max = 50, message = "email should not be more than 50 characters.")
    @Email(message = "Email is Not Valid!")
    private String emailAddress;
    @NotNull(message = "isPrimary should not be null.")
    @JsonProperty("isPrimary")
    private boolean primary;

}
