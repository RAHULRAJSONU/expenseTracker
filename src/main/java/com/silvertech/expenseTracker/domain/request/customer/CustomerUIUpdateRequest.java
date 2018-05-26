package com.silvertech.expenseTracker.domain.request.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerUIUpdateRequest {

    @NotNull
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("gender")
    private UUID gender;

    @JsonProperty("dob")
    private long dob;

    @JsonProperty("user")
    private String user;

}
