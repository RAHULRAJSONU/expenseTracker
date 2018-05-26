package com.silvertech.expenseTracker.domain.request.account;

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
public class AccountUIUpdateRequest {

    @NotNull
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("user")
    private String user;

}
