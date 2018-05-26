package com.silvertech.expenseTracker.domain.response.customer;


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
public class CustomerCreateResponse {

    public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private Integer httpStatus;
    @JsonProperty("ts")
    private String timeStamp = new DateTime().toDateTimeISO().toString(ISO_DATETIME_PATTERN);

    private List<CustomerResponseList> customerResponseLists;

    public CustomerCreateResponse(int httpStatus, List<CustomerResponseList> customerResponseLists) {
        this.httpStatus = httpStatus;
        this.customerResponseLists = customerResponseLists;
    }
}
