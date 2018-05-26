package com.silvertech.expenseTracker.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateUpdateResponse {

    public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private int httpStatus;

    @JsonProperty("ts")
    private String timeStamp = new DateTime().toDateTimeISO().toString(ISO_DATETIME_PATTERN);

    public CreateUpdateResponse(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
