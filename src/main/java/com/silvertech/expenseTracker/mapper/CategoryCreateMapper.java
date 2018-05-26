package com.silvertech.expenseTracker.mapper;

import com.silvertech.expenseTracker.domain.request.CategoryCreateRequest;
import com.silvertech.expenseTracker.domain.request.CategoryUICreateRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryCreateMapper {


    public CategoryUICreateRequest createCategoryCreateRequest
            (CategoryCreateRequest categoryCreateRequest, String userId) {
        CategoryUICreateRequest categoryUICreateRequest = new CategoryUICreateRequest();
        categoryUICreateRequest.setName(categoryCreateRequest.getName());
        categoryUICreateRequest.setActive(categoryCreateRequest.getActive());
        categoryUICreateRequest.setDescription(categoryCreateRequest.getDescription());
        if (null != categoryCreateRequest.getParent()) {
            categoryUICreateRequest.setParentId(UUID.fromString(categoryCreateRequest.getParent()));
        }
        categoryUICreateRequest.setUser(userId);
        return categoryUICreateRequest;
    }
}
