package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CustomerCommonService {

    CustomerRepository customerRepository;

    public Customer getCustomerById(UUID customerId) {
        log.info("Find Customer by Id " + customerId);
        Customer customer = customerRepository.getOne(customerId);
        log.info("getCustomerById " + customer);
        if (null == customer) {
            log.error("Customer not Found");
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.CUSTOMER_NOT_FOUND);
        }
        log.info("Output getCustomerById {} ", customerId);
        return customer;
    }

}
