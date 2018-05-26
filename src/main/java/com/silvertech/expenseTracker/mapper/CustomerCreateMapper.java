package com.silvertech.expenseTracker.mapper;

import com.silvertech.expenseTracker.domain.request.customer.CustomerCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerUICreateRequest;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerCreateMapper {


    public CustomerUICreateRequest createCustomerCreateRequest
            (CustomerCreateRequest customerCreateRequest, String userId) {
        CustomerUICreateRequest customerUICreateRequest = new CustomerUICreateRequest();
        customerUICreateRequest.setFirstName(customerCreateRequest.getFirstName());
        if (null != customerCreateRequest.getMiddleName()) {
            customerUICreateRequest.setMiddleName(customerCreateRequest.getMiddleName());
        }
        customerUICreateRequest.setLastName(customerCreateRequest.getLastName());
        customerUICreateRequest.setDob((new DateFormatUtil()
                .parseDate(customerCreateRequest.getDob())).getTime());
        customerUICreateRequest.setGender(UUID.fromString(customerCreateRequest.getGender()));
        customerUICreateRequest.setAddressList(customerCreateRequest.getAddress());
        customerUICreateRequest.setMobileList(customerCreateRequest.getMobiles());
        customerUICreateRequest.setEmailList(customerCreateRequest.getEmails());
        customerUICreateRequest.setUser(userId);
        return customerUICreateRequest;
    }
}
