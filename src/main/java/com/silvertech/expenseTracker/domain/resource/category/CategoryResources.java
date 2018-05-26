package com.silvertech.expenseTracker.domain.resource.category;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import lombok.Getter;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;

import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;


@Getter
public enum CategoryResources {

    SELF("self") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<CategoryProjection> resource,
                                    CategoryProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Category.class,
                        userProjection.getId()).withSelfRel());
            }

        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Category> resource,
                                    Category node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Category.class,
                        node.getId()).withSelfRel());
            }

        }
    },
    CATEGORY("category") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<CategoryProjection> resource,
                                    CategoryProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                TemplateVariable projection = new TemplateVariable("projection", REQUEST_PARAM);
                TemplateVariables vars = new TemplateVariables(projection);
                UriTemplate uri = new UriTemplate(resource.getId().getHref(), vars);
                resource.add(new Link(uri, getLinkName()));
            }

        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Category> resource,
                                    Category node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Category.class,
                        node.getId())
                        .slash(getLinkName())
                        .withRel(getLinkName()));
            }

        }
    },
    PARENT_CATEGORY("parent-nodes"),
    CHILDREN("children") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<CategoryProjection> resource,
                                    CategoryProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Category.class,
                        userProjection.getId()).withRel(getLinkName()));
            }


        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Category> resource,
                                    Category node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Category.class,
                        node.getParent().getId()).withRel(getLinkName()));
            }

        }
    };

    private String linkName;

    CategoryResources(String linkName) {
        this.linkName = linkName;
    }

    public void addResourceLink(EntityLinks entityLinks, Resource<CategoryProjection> resource,
                                CategoryProjection userProjection) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Category.class,
                    userProjection.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }

    public void addResourceLink(EntityLinks entityLinks, Resource<Category> resource,
                                Category node) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Category.class,
                    node.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }


}
