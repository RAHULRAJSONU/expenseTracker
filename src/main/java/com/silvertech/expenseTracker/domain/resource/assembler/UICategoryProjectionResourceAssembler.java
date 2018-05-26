package com.silvertech.expenseTracker.domain.resource.assembler;


import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import com.silvertech.expenseTracker.domain.resource.category.CategoryResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

import static com.silvertech.expenseTracker.domain.resource.category.CategoryResources.CATEGORY;
import static com.silvertech.expenseTracker.domain.resource.category.CategoryResources.CHILDREN;
import static com.silvertech.expenseTracker.domain.resource.category.CategoryResources.PARENT_CATEGORY;
import static com.silvertech.expenseTracker.domain.resource.category.CategoryResources.SELF;

@Component
public class UICategoryProjectionResourceAssembler
        implements ResourceAssembler<CategoryProjection, Resource<CategoryProjection>> {

    private static final EnumSet<CategoryResources> toAddForSearch
            = EnumSet.of(SELF, CATEGORY, PARENT_CATEGORY, CHILDREN);
    @Autowired
    private EntityLinks entityLinks;

    @Override
    public Resource<CategoryProjection> toResource(CategoryProjection entity) {
        Resource<CategoryProjection> resource = new Resource<CategoryProjection>(entity);
        for (CategoryResources resourceName : toAddForSearch) {
            resourceName.addResourceLink(entityLinks, resource, entity);
        }
        return resource;
    }
}