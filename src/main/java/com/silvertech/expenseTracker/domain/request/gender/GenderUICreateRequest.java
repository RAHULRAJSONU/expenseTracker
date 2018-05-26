package com.silvertech.expenseTracker.domain.request.gender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GenderUICreateRequest {

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("user")
    private String user;

}
