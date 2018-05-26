package com.silvertech.expenseTracker.domain.resource;


import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.response.CategoryResponse;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class CategoryCreateResponseBuilder {

    public CategoryResponse createCaategoryResponse(Category category) {
        CategoryResponse categoryResponse = CategoryResponse.builder()
                ._id(category.getId())
                .parentId(getParent(category))
                .build();
        Link selfLink = linkTo(Category.class)
                .slash("categories")
                .slash(categoryResponse.get_id())
                .slash("?projection=categoryProjection")
                .withSelfRel();

        categoryResponse.add(selfLink);
        return categoryResponse;
    }

    private String getParent(Category category) {
        return null != category.getParent() ? category.getParent().getId().toString() : null;
    }

}
