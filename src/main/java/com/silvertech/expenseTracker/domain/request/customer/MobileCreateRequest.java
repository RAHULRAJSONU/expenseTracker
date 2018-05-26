package com.silvertech.expenseTracker.domain.request.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MobileCreateRequest {

    @JsonProperty("countryCode")
    @Size(max = 4, message = "country code should not be more than 4 characters.")
    private String countryCode;
    @JsonProperty("mobileNumber")
    @Size(max = 14, message = "mobile number should not be more than 14 characters.")
    private String mobileNumber;
    @NotNull(message = "isPrimary should not be null.")
    @JsonProperty("isPrimary")
    private boolean primary;

}
