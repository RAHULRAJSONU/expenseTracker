package com.silvertech.expenseTracker.domain.resource.account;

import com.silvertech.expenseTracker.domain.entity.AccountProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

import static com.silvertech.expenseTracker.domain.resource.account.AccountResources.CHILDREN;
import static com.silvertech.expenseTracker.domain.resource.account.AccountResources.PARENT_ACCOUNT;


@Component
public class UIAccountProjectionResourceProcessor implements
        ResourceProcessor<Resource<AccountProjection>> {

    private static final EnumSet<AccountResources> toAddForDataRest
            = EnumSet.of(PARENT_ACCOUNT, CHILDREN);
    private static final EnumSet<AccountResources> toAddForController
            = EnumSet.allOf(AccountResources.class);
    @Autowired
    private EntityLinks entityLinks;

    @Override
    public Resource<AccountProjection> process(Resource<AccountProjection> resource) {
        AccountProjection userProjection = resource.getContent();
        for (AccountResources resourceName : toAddForDataRest) {
            resourceName.addResourceLink(entityLinks, resource, userProjection);
        }
        return resource;
    }


    public Resource<AccountProjection> processTypeAndLevelResource(Resource<AccountProjection> resource) {
        AccountProjection userProjection = resource.getContent();
        for (AccountResources resourceName : toAddForController) {
            resourceName.addResourceLink(entityLinks, resource, userProjection);
        }
        return resource;
    }
}