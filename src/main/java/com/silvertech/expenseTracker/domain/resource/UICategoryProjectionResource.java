package com.silvertech.expenseTracker.domain.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.silvertech.expenseTracker.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonRootName("categories")
public class UICategoryProjectionResource extends ResourceSupport {
    @JsonProperty("id")
    private UUID _id;
    private String name;
    private String description;
    private Category parentId;
    @JsonProperty("active")
    private boolean active;
    private Date createDateTime;
    private Date lastModifiedDateTime;
    private String lastModifiedUser;
}
