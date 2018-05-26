package com.silvertech.expenseTracker.domain.resource.assembler;


import com.silvertech.expenseTracker.domain.entity.CustomerProjection;
import com.silvertech.expenseTracker.domain.resource.customer.CustomerResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

import static com.silvertech.expenseTracker.domain.resource.customer.CustomerResources.CUSTOMER;
import static com.silvertech.expenseTracker.domain.resource.customer.CustomerResources.SELF;

@Component
public class UICustomerProjectionResourceAssembler
        implements ResourceAssembler<CustomerProjection, Resource<CustomerProjection>> {

    private static final EnumSet<CustomerResources> toAddForSearch
            = EnumSet.of(SELF, CUSTOMER);
    @Autowired
    private EntityLinks entityLinks;

    @Override
    public Resource<CustomerProjection> toResource(CustomerProjection entity) {
        Resource<CustomerProjection> resource = new Resource<CustomerProjection>(entity);
        for (CustomerResources resourceName : toAddForSearch) {
            resourceName.addResourceLink(entityLinks, resource, entity);
        }
        return resource;
    }
}