package com.silvertech.expenseTracker.domain.request.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerUICreateRequest {

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("dob")
    private long dob;

    @JsonProperty("gender")
    private UUID gender;

    @JsonProperty("addressList")
    private List<AddressCreateRequest> addressList;

    @JsonProperty("mobileList")
    private List<MobileCreateRequest> mobileList;

    @JsonProperty("emailList")
    private List<EmailCreateRequest> emailList;

    @JsonProperty("user")
    private String user;

}
