package com.silvertech.expenseTracker.service;


import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.entity.Gender;
import com.silvertech.expenseTracker.domain.request.customer.CustomerUIUpdateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerUpdateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerUpdateRequestList;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import com.silvertech.expenseTracker.validator.CustomerValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CustomerUpdateService {

    private CustomerValidator customerValidator;

    private CustomerService customerService;

    private CustomerCommonService customerCommonService;

    private GenderService genderService;

    private CustomerRepository customerRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public CreateUpdateResponse updateCustomer
            (List<CustomerUpdateRequestList> customerUpdateRequestLists, String userId) {
        List<UUID> updateCustomerIds = new ArrayList<>();
        log.info("customer update request {} "
                , customerUpdateRequestLists);
        for (CustomerUpdateRequestList customerUpdateRequestList : customerUpdateRequestLists) {

            for (CustomerUpdateRequest customerUpdateRequest
                    : customerUpdateRequestList.getCustomerUpdateRequests()) {
                customerValidator.validateCustomerUpdateRequest(customerUpdateRequest);

                UUID updateCustomerId = processUpdateCustomer(
                        buildCustomerUpdateRequest(customerUpdateRequest, userId));

                updateCustomerIds.add(updateCustomerId);
            }
        }
        log.info("Output updateCategory HTTP Status code {} ",
                HttpStatus.OK.value());
        /*applicationEventPublisher.publishEvent(new AfterSaveEvent(CategoryUpdateEvent.builder()
                .updateCustomerIds(updateCustomerIds).build()));*/
        return new CreateUpdateResponse(HttpStatus.OK.value());
    }

    public UUID processUpdateCustomer(CustomerUIUpdateRequest customerUIUpdateRequest) {

        log.info("UpdateCustomer request received{} ", customerUIUpdateRequest);
        UUID customerId = customerUIUpdateRequest.getId();
        Customer customer = customerCommonService.getCustomerById(customerId);

        if (validateCustomerUpdate(customer, customerUIUpdateRequest)) {
            log.info(" validateCustomerUpdate returned true:No Updates " +
                    "found for customer table");
            return customer.getId();
        } else {
            customer.setFirstName(customerUIUpdateRequest.getFirstName().toUpperCase());
            if (null != customerUIUpdateRequest.getMiddleName()) {
                customer.setMiddleName(customerUIUpdateRequest.getMiddleName().toUpperCase());
            }
            customer.setLastName(customerUIUpdateRequest.getLastName().toUpperCase());
            customer.setDob(customerUIUpdateRequest.getDob());
            Gender gender = genderService.getById(customerUIUpdateRequest.getGender());
            if (null != gender) {
                customer.setGender(gender);
            }
            if (customerUIUpdateRequest.getUser() != null) {
                customer.setLastModifiedUser(customerUIUpdateRequest.getUser());
            }

        }
        customer.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());

        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Output updateCustomer Id {} ", updatedCustomer.getId());
        return updatedCustomer.getId();
    }

    private boolean validateCustomerUpdate(Customer customer,
                                           CustomerUIUpdateRequest customerUIUpdateRequest) {
        boolean noFirstNameUpdate = validateCustomerFirstName(customer,
                customerUIUpdateRequest);
        boolean noMiddleNameUpdate = validateCustomerMiddleName
                (customer, customerUIUpdateRequest);
        boolean noLastNameUpdate = validateCustomerLastName(customer, customerUIUpdateRequest);
        boolean noDobUpdate = validateCustomerDob(customer, customerUIUpdateRequest);
        boolean noGenderUpdate = validateCustomerGender(customer, customerUIUpdateRequest);

        if (noFirstNameUpdate && noMiddleNameUpdate && noLastNameUpdate && noDobUpdate && noGenderUpdate) {
            return true;
        }
        return false;

    }

    private boolean validateCustomerFirstName(Customer customer,
                                              CustomerUIUpdateRequest
                                                      customerUIUpdateRequest) {
        boolean noFirstNameUpdate = false;
        if (null == customerUIUpdateRequest.getFirstName()) {
            noFirstNameUpdate = true;
        } else if (customer.getFirstName()
                .equalsIgnoreCase(customerUIUpdateRequest.getFirstName())) {
            noFirstNameUpdate = true;
        }
        return noFirstNameUpdate;
    }

    private boolean validateCustomerLastName(Customer customer,
                                             CustomerUIUpdateRequest
                                                     customerUIUpdateRequest) {
        boolean noLastNameUpdate = false;
        if (null == customerUIUpdateRequest.getLastName()) {
            noLastNameUpdate = true;
        } else if (customer.getLastName()
                .equalsIgnoreCase(customerUIUpdateRequest.getLastName())) {
            noLastNameUpdate = true;
        }
        return noLastNameUpdate;
    }

    private boolean validateCustomerMiddleName(Customer customer,
                                               CustomerUIUpdateRequest
                                                       customerUIUpdateRequest) {
        boolean noMiddleNameUpdate = false;
        if (null == customerUIUpdateRequest.getMiddleName()) {
            noMiddleNameUpdate = true;
        } else if (customer.getMiddleName()
                .equalsIgnoreCase(customerUIUpdateRequest.getMiddleName())) {
            noMiddleNameUpdate = true;
        }
        return noMiddleNameUpdate;
    }

    private boolean validateCustomerDob(Customer customer,
                                        CustomerUIUpdateRequest
                                                customerUIUpdateRequest) {
        boolean noDobUpdate = false;
        if (customer.getDob() == customerUIUpdateRequest.getDob()) {
            noDobUpdate = true;
        }
        return noDobUpdate;
    }

    private boolean validateCustomerGender(Customer customer,
                                           CustomerUIUpdateRequest
                                                   customerUIUpdateRequest) {
        boolean noGenderUpdate = false;
        if (null == customerUIUpdateRequest.getGender()) {
            noGenderUpdate = true;
        } else if (customer.getGender().getId().equals(customerUIUpdateRequest.getGender())) {
            noGenderUpdate = true;
        }
        return noGenderUpdate;
    }


    private CustomerUIUpdateRequest buildCustomerUpdateRequest(
            CustomerUpdateRequest customerUpdateRequest, String userId) {

        CustomerUIUpdateRequest customerUIUpdateRequest = new CustomerUIUpdateRequest();
        customerUIUpdateRequest.setId(UUID.fromString(customerUpdateRequest.getId()));
        customerUIUpdateRequest.setFirstName(customerUpdateRequest.getFirstName());
        if (null != customerUpdateRequest.getMiddleName()) {
            customerUIUpdateRequest.setMiddleName(customerUpdateRequest.getMiddleName());
        }
        customerUIUpdateRequest.setLastName(customerUpdateRequest.getLastName());
        customerUIUpdateRequest.setDob((new DateFormatUtil())
                .parseDate(customerUpdateRequest.getDob()).getTime());
        customerUIUpdateRequest.setGender(UUID.fromString(customerUpdateRequest.getGender()));
        customerUIUpdateRequest.setUser(userId);
        return customerUIUpdateRequest;
    }
}
