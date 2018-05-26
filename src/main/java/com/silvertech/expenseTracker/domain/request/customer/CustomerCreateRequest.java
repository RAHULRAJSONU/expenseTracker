package com.silvertech.expenseTracker.domain.request.customer;

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
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerCreateRequest {

    @NotNull(message = "Customer first name should not be null.")
    @JsonProperty("firstName")
    @Size(max = 20, message = "First name should not be more than 20 characters.")
    private String firstName;

    @JsonProperty("middleName")
    @Size(max = 20, message = "Middle name should not be more than 20 characters.")
    private String middleName;

    @NotNull(message = "Customer last name should not be null.")
    @JsonProperty("lastName")
    @Size(max = 20, message = "Last name should not be more than 20 characters.")
    private String lastName;

    @NotNull(message = "Category name should not be null.")
    @JsonProperty("dateOfBirth")
    @Size(max = 20, message = "name should not be more than 20 characters.")
    private String dob;

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "GenderId should match ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$.")
    @JsonProperty("genderId")
    private String gender;

    @JsonProperty("addressList")
    private List<AddressCreateRequest> address;

    @JsonProperty("mobileList")
    private List<MobileCreateRequest> mobiles;

    @JsonProperty("emailList")
    private List<EmailCreateRequest> emails;

}
