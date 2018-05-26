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
public class AddressCreateRequest {

    @NotNull(message = "country name should not be null.")
    @JsonProperty("country")
    @Size(max = 25, message = "country should not be more than 25 characters.")
    private String country;

    @JsonProperty("state")
    @Size(max = 25, message = "state name should not be more than 25 characters.")
    private String state;

    @NotNull(message = "city name should not be null.")
    @JsonProperty("city")
    @Size(max = 25, message = "city name should not be more than 25 characters.")
    private String city;

    @NotNull(message = "streetAddress should not be null.")
    @JsonProperty("streetAddress")
    @Size(max = 50, message = "streetAddress should not be more than 50 characters.")
    private String streetAddress;

    @NotNull(message = "zip should not be null.")
    @JsonProperty("zip")
    private String zip;

    @NotNull(message = "addressType should not be null.")
    @JsonProperty("addressType")
    private String addressType;

}
