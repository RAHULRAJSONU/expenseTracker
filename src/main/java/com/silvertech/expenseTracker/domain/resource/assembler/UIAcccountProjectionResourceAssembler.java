package com.silvertech.expenseTracker.domain.resource.assembler;


import com.silvertech.expenseTracker.domain.entity.AccountProjection;
import com.silvertech.expenseTracker.domain.resource.account.AccountResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

import static com.silvertech.expenseTracker.domain.resource.account.AccountResources.ACCOUNT;
import static com.silvertech.expenseTracker.domain.resource.account.AccountResources.CHILDREN;
import static com.silvertech.expenseTracker.domain.resource.account.AccountResources.PARENT_ACCOUNT;
import static com.silvertech.expenseTracker.domain.resource.account.AccountResources.SELF;

@Component
public class UIAcccountProjectionResourceAssembler
        implements ResourceAssembler<AccountProjection, Resource<AccountProjection>> {

    private static final EnumSet<AccountResources> toAddForSearch
            = EnumSet.of(SELF, ACCOUNT, PARENT_ACCOUNT, CHILDREN);
    @Autowired
    private EntityLinks entityLinks;

    @Override
    public Resource<AccountProjection> toResource(AccountProjection entity) {
        Resource<AccountProjection> resource = new Resource<AccountProjection>(entity);
        for (AccountResources resourceName : toAddForSearch) {
            resourceName.addResourceLink(entityLinks, resource, entity);
        }
        return resource;
    }
}