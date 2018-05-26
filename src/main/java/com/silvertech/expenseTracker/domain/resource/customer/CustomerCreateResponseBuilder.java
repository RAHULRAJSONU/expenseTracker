package com.silvertech.expenseTracker.domain.resource.customer;


import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.response.customer.CustomerResponse;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class CustomerCreateResponseBuilder {

    public CustomerResponse createCustomerResponse(Customer customer) {
        CustomerResponse customerResponse = CustomerResponse.builder()
                ._id(customer.getId())
                .fullName(customer.getFullName())
                .build();
        Link selfLink = linkTo(Customer.class)
                .slash("customers")
                .slash(customerResponse.get_id())
                .slash("?projection=customerProjection")
                .withSelfRel();

        customerResponse.add(selfLink);
        return customerResponse;
    }

}
