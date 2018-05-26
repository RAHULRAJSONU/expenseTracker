package com.silvertech.expenseTracker.domain.resource.assembler;

import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import com.silvertech.expenseTracker.domain.resource.category.CategoryResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

import static com.silvertech.expenseTracker.domain.resource.category.CategoryResources.CHILDREN;
import static com.silvertech.expenseTracker.domain.resource.category.CategoryResources.PARENT_CATEGORY;


@Component
public class UICategoryProjectionResourceProcessor implements
        ResourceProcessor<Resource<CategoryProjection>> {

    private static final EnumSet<CategoryResources> toAddForDataRest
            = EnumSet.of(PARENT_CATEGORY, CHILDREN);
    private static final EnumSet<CategoryResources> toAddForController
            = EnumSet.allOf(CategoryResources.class);
    @Autowired
    private EntityLinks entityLinks;

    @Override
    public Resource<CategoryProjection> process(Resource<CategoryProjection> resource) {
        CategoryProjection userProjection = resource.getContent();
        for (CategoryResources resourceName : toAddForDataRest) {
            resourceName.addResourceLink(entityLinks, resource, userProjection);
        }
        return resource;
    }


    public Resource<CategoryProjection> processTypeAndLevelResource(Resource<CategoryProjection> resource) {
        CategoryProjection userProjection = resource.getContent();
        for (CategoryResources resourceName : toAddForController) {
            resourceName.addResourceLink(entityLinks, resource, userProjection);
        }
        return resource;
    }
}