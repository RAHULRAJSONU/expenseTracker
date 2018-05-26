package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.request.customer.CustomerCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerCreateRequestList;
import com.silvertech.expenseTracker.domain.response.customer.CustomerCreateResponse;
import com.silvertech.expenseTracker.domain.response.customer.CustomerResponse;
import com.silvertech.expenseTracker.domain.response.customer.CustomerResponseList;
import com.silvertech.expenseTracker.mapper.CustomerCreateMapper;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import com.silvertech.expenseTracker.validator.CustomerValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CustomerCreateService {

    List<CustomerResponse> customerResponses;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerValidator customerValidator;
    @Autowired
    private CustomerCreateMapper customerCreateMapper;
    @Autowired
    private CustomerService customerService;

    @Transactional
    public CustomerCreateResponse createCustomer
            (List<CustomerCreateRequestList> customerCreateRequestLists, String userId) {
        List<CustomerResponseList> customerResponseLists = new ArrayList<>();
        log.info("Input createCustomer {} ", customerCreateRequestLists);
        Map<String, Customer> customerHashMap = customerValidator.
                validateCustomerRequest(customerCreateRequestLists);
        for (CustomerCreateRequestList customerCreateRequestList : customerCreateRequestLists) {
            CustomerResponseList customerResponseList = new CustomerResponseList();
            customerResponses = new ArrayList<CustomerResponse>();
            log.info("Create Request for Customer " + customerCreateRequestList);
            createNewCustomer(customerCreateRequestList, customerHashMap, userId);
            customerResponseList.setCustomerResponses(customerResponses);
            customerResponseLists.add(customerResponseList);
        }
        log.info("Output createCustomer HTTP Response {} ", HttpStatus.CREATED.value());
        /*applicationEventPublisher.publishEvent(new AfterCreateEvent(CustomerCreateEvent.builder()
                .CustomerCreateResponse(genderResponseLists).build()));*/
        return new CustomerCreateResponse(HttpStatus.CREATED.value(), customerResponseLists);
    }

    private void createNewCustomer(CustomerCreateRequestList customerCreateRequestList,
                                   Map<String, Customer> customerMap, String userId) {

        List<Customer> customerList = new ArrayList<>();

        List<Customer> responseCustomerList;
        for (CustomerCreateRequest customerCreateRequest1
                : customerCreateRequestList.getCustomerCreateRequests()) {
            Customer customer = customerService.callCreateCustomerService(
                    customerCreateRequest1, userId);
            customerList.add(customer);
        }
        responseCustomerList = customerService.saveAllCustomer(customerList);
        customerService.buildCustomerResponses(responseCustomerList, customerResponses);
    }
}
