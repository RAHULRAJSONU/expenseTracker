package com.silvertech.expenseTracker.domain.specification.transaction;

import com.silvertech.expenseTracker.domain.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificationsBuilder {
     
    private final List<SearchCriteria> params;
 
    public UserSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }
 
    public UserSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }
 
    public Specification<Transaction> build() {
        if (params.size() == 0) {
            return null;
        }
 
        List<Specification<Transaction>> specs = new ArrayList<Specification<Transaction>>();
        for (SearchCriteria param : params) {
            specs.add(new TransactionSpecification(param));
        }
 
        Specification<Transaction> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}