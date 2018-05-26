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


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerUpdateRequest {

    @NotNull(message = "CustomerId cannot be null.")
    @JsonProperty("id")
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "Customer id should match pattern:" +
                    "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}.")
    private String id;

    @NotNull(message = "Customer firstName should not be null.")
    @JsonProperty("firstName")
    @Size(max = 20, message = "firstName should not be more than 20 characters.")
    private String firstName;

    @JsonProperty("middleName")
    @Size(max = 20, message = "middleName should not be more than 20 characters.")
    private String middleName;

    @NotNull(message = "Customer lastName should not be null.")
    @JsonProperty("lastName")
    @Size(max = 20, message = "lastName should not be more than 20 characters.")
    private String lastName;

    @NotNull(message = "Customer dob should not be null.")
    @JsonProperty("dob")
    @Size(max = 10, message = "date of birth should not be more than 10 characters.")
    private String dob;

    @NotNull(message = "gender should not be null.")
    @Size(max = 50, message = "gender should not be more than 50 characters.")
    @JsonProperty("gender")
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "Customer gender should match pattern:" +
                    "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}.")
    private String gender;

}
