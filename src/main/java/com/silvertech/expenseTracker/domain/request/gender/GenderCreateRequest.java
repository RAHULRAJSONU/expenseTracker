package com.silvertech.expenseTracker.domain.request.gender;

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
public class GenderCreateRequest {

    @NotNull(message = "sex should not be null.")
    @JsonProperty("sex")
    @Size(max = 10, message = "sex should not be more than 10 characters.")
    private String sex;

}
