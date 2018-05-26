package com.silvertech.expenseTracker.domain.resource.transaction;

import com.silvertech.expenseTracker.domain.entity.Transaction;
import com.silvertech.expenseTracker.domain.entity.TransactionProjection;
import lombok.Getter;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;


@Getter
public enum TransactionResources {

    SELF("self") {
        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<TransactionProjection> resource,
                                    TransactionProjection userProjection) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Transaction.class,
                        userProjection.getId()).withSelfRel());
            }

        }

        @Override
        public void addResourceLink(EntityLinks entityLinks, Resource<Transaction> resource,
                                    Transaction node) {
            if (!resource.hasLink(getLinkName())) {
                resource.add(entityLinks.linkForSingleResource(Transaction.class,
                        node.getId()).withSelfRel());
            }

        }
    };

    private String linkName;

    TransactionResources(String linkName) {
        this.linkName = linkName;
    }

    public void addResourceLink(EntityLinks entityLinks, Resource<TransactionProjection> resource,
                                TransactionProjection userProjection) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Transaction.class,
                    userProjection.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }

    public void addResourceLink(EntityLinks entityLinks, Resource<Transaction> resource,
                                Transaction node) {
        if (!resource.hasLink(getLinkName())) {
            resource.add(entityLinks.linkForSingleResource(Transaction.class,
                    node.getId()).slash(
                    getLinkName()).withRel(getLinkName()));
        }

    }


}
