package com.silvertech.expenseTracker.domain.request;

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
public class CategoryCreateRequest {

    @NotNull(message = "Category name should not be null.")
    @JsonProperty("categoryName")
    @Size(max = 20, message = "name should not be more than 20 characters.")
    private String name;

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "ParentId should match ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$.")
    @JsonProperty("parentId")
    private String parent;

    @NotNull(message = "Description should not be null.")
    @Size(max = 50, message = "description should not be more than 50 characters.")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "isPrimary should not be null.")
    @JsonProperty("isActive")
    private Boolean active;

}
