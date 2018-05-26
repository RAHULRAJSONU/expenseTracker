package com.silvertech.expenseTracker.domain.resource.account;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.AccountProjection;
import lombok.Getter;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;


@Getter
public enum AccountResources {

    SELF("self") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<AccountProjection> resource,
                                    AccountProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Account.class,
                        userProjection.getId()).withSelfRel());
            }

        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Account> resource,
                                    Account node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Account.class,
                        node.getId()).withSelfRel());
            }

        }
    },
    ACCOUNT("account") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<AccountProjection> resource,
                                    AccountProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                TemplateVariable projection = new TemplateVariable("projection", REQUEST_PARAM);
                TemplateVariables vars = new TemplateVariables(projection);
                UriTemplate uri = new UriTemplate(resource.getId().getHref(), vars);
                resource.add(new Link(uri, getLinkName()));
            }

        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Account> resource,
                                    Account node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Account.class,
                        node.getId())
                        .slash(getLinkName())
                        .withRel(getLinkName()));
            }

        }
    },
    PARENT_ACCOUNT("parent-accounts"),
    CHILDREN("children") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<AccountProjection> resource,
                                    AccountProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Account.class,
                        userProjection.getId()).withRel(getLinkName()));
            }


        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Account> resource,
                                    Account node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Account.class,
                        node.getParentAccount().getId()).withRel(getLinkName()));
            }

        }
    };

    private String linkName;

    AccountResources(String linkName) {
        this.linkName = linkName;
    }

    public void addResourceLink(EntityLinks entityLinks, Resource<AccountProjection> resource,
                                AccountProjection userProjection) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Account.class,
                    userProjection.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }

    public void addResourceLink(EntityLinks entityLinks, Resource<Account> resource,
                                Account node) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Account.class,
                    node.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }


}
