package com.silvertech.expenseTracker.domain.resource.customer;

import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.entity.CustomerProjection;
import lombok.Getter;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;


@Getter
public enum CustomerResources {

    SELF("self") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<CustomerProjection> resource,
                                    CustomerProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Customer.class,
                        userProjection.getId()).withSelfRel());
            }

        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Customer> resource,
                                    Customer node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Customer.class,
                        node.getId()).withSelfRel());
            }

        }
    },
    CUSTOMER("customer") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<CustomerProjection> resource,
                                    CustomerProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                TemplateVariable projection = new TemplateVariable("projection", REQUEST_PARAM);
                TemplateVariables vars = new TemplateVariables(projection);
                UriTemplate uri = new UriTemplate(resource.getId().getHref(), vars);
                resource.add(new Link(uri, getLinkName()));
            }

        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Customer> resource,
                                    Customer node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Customer.class,
                        node.getId())
                        .slash(getLinkName())
                        .withRel(getLinkName()));
            }

        }
    };

    private String linkName;

    CustomerResources(String linkName) {
        this.linkName = linkName;
    }

    public void addResourceLink(EntityLinks entityLinks, Resource<CustomerProjection> resource,
                                CustomerProjection userProjection) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Customer.class,
                    userProjection.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }

    public void addResourceLink(EntityLinks entityLinks, Resource<Customer> resource,
                                Customer node) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Customer.class,
                    node.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }


}
