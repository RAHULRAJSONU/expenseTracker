package com.silvertech.expenseTracker.domain.response.gender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GenderResponseList {

    @JsonProperty("genderResponse")
    private List<GenderResponse> genderResponses;

}
