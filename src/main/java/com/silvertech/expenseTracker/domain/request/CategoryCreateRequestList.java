package com.silvertech.expenseTracker.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryCreateRequestList {
    @Valid
    @NotNull(message = "CategoryRequests should not be null.")
    private List<CategoryCreateRequest> categoryCreateRequests;
}
