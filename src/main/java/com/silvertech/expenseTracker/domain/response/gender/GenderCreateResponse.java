package com.silvertech.expenseTracker.domain.response.gender;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GenderCreateResponse {

    public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private Integer httpStatus;
    @JsonProperty("ts")
    private String timeStamp = new DateTime().toDateTimeISO().toString(ISO_DATETIME_PATTERN);

    private List<GenderResponseList> genderResponseLists;

    public GenderCreateResponse(int httpStatus, List<GenderResponseList> genderResponseLists) {
        this.httpStatus = httpStatus;
        this.genderResponseLists = genderResponseLists;
    }
}
