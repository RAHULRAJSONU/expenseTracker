package com.silvertech.expenseTracker.validator;

import com.silvertech.expenseTracker.domain.entity.AddressType;
import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.entity.Gender;
import com.silvertech.expenseTracker.domain.request.customer.AddressCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerCreateRequestList;
import com.silvertech.expenseTracker.domain.request.customer.CustomerUpdateRequest;
import com.silvertech.expenseTracker.domain.request.customer.EmailCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.MobileCreateRequest;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorCodeValidationException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import com.silvertech.expenseTracker.service.GenderService;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Component
@Slf4j
public class CustomerValidator {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private GenderService genderService;

    public void validateCustomerUpdateRequest(
            CustomerUpdateRequest customerUpdateRequest) {
        if (null == customerUpdateRequest.getId() &&
                null == customerUpdateRequest.getFirstName() &&
                null == customerUpdateRequest.getLastName() &&
                null == customerUpdateRequest.getDob() &&
                null == customerUpdateRequest.getGender()) {
            throw new ErrorCodeException(HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_UPDATE);
        }
    }

    public Map<String, Customer> validateCustomerRequest(
            List<CustomerCreateRequestList> customerCreateRequestLists) {
        if (CollectionUtils.isEmpty(customerCreateRequestLists)) {
            throw new ErrorCodeException
                    (HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_CREATE);
        } else {
            return validateCustomerCreateRequest(customerCreateRequestLists);
        }
    }

    private Map<String, Customer> validateCustomerCreateRequest(
            List<CustomerCreateRequestList> customerCreateRequestLists) {
        Map<String, Customer> customerHashMap = new HashMap<String, Customer>();
        Set<ErrorMessage> errorMessageSet = new HashSet();
        for (CustomerCreateRequestList customerCreateRequestList :
                customerCreateRequestLists) {
            validateCustomerRequestList(customerCreateRequestList, errorMessageSet, customerHashMap);
        }

        if (errorMessageSet.isEmpty()) {
            return customerHashMap;
        } else {
            List<ErrorMessage> errorMessageList = new ArrayList<>();
            errorMessageList.addAll(errorMessageSet);
            throw new ErrorCodeValidationException(HttpStatus.BAD_REQUEST, errorMessageList);
        }
    }

    private void validateCustomerRequestList(CustomerCreateRequestList customerCreateRequestList,
                                             Set<ErrorMessage> errorMessageSet,
                                             Map<String, Customer> customerMap) {
        for (CustomerCreateRequest customerCreateRequest
                : customerCreateRequestList.getCustomerCreateRequests()) {
            validateForUniqueCustomer(
                    customerCreateRequest, errorMessageSet, customerMap);
            validateGender(customerCreateRequest, errorMessageSet);
            validateAddress(customerCreateRequest, errorMessageSet);
            validateEmail(customerCreateRequest, errorMessageSet);
            validateMobile(customerCreateRequest, errorMessageSet);
        }
    }

    private void validateGender(CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet) {
        log.info("validating gender" + " : " + customerCreateRequest);
        if (null != customerCreateRequest.getGender()) {
            Gender gender = genderService.getById(UUID.fromString(customerCreateRequest.getGender()));
            if (null == gender) {
                log.error(Constants.BAD_REQUEST + ", " + Constants.GENDER_NOT_FOUND + " : "
                        + customerCreateRequest.getGender());
                errorMessageSet.add(new ErrorMessage(Constants.GENDER_NOT_FOUND,
                        customerCreateRequest.getGender()));
            }
        } else {
            log.error(Constants.BAD_REQUEST + ", " + Constants.GENDER_MUST_BE_PRESENT + " : "
                    + customerCreateRequest.getGender());
            errorMessageSet.add(new ErrorMessage(Constants.GENDER_MUST_BE_PRESENT,
                    customerCreateRequest.getGender()));
        }
    }

    private void validateMobile(CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet) {
        log.info("validating mobile" + " : " + customerCreateRequest.getMobiles());
        if (customerCreateRequest.getMobiles().isEmpty()) {
            log.error(Constants.BAD_REQUEST + ", " + Constants.ATLEAST_ONE_MOBILE_SHOULD_BE_PRESENT + " : "
                    + customerCreateRequest.getMobiles().toString());
            errorMessageSet.add(new ErrorMessage(Constants.ATLEAST_ONE_MOBILE_SHOULD_BE_PRESENT,
                    customerCreateRequest.getMobiles().toString()));
        } else {
            checkForMultiplePrimaryMobile(customerCreateRequest, errorMessageSet);
        }
    }

    private void checkForMultiplePrimaryMobile(CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet) {
        int primary = 0;
        for (MobileCreateRequest mobileCreateRequest : customerCreateRequest.getMobiles()) {
            log.info("checking for multiple primary mobile" + " : " + customerCreateRequest.getMobiles());
            if (mobileCreateRequest.isPrimary()) {
                primary++;
            }
        }
        if (1 < primary) {
            log.error(Constants.BAD_REQUEST + ", " + Constants.CAN_NOT_SET_MULTIPLE_PRIMARY_MOBILE + " : "
                    + customerCreateRequest.getMobiles().toString());
            errorMessageSet.add(new ErrorMessage(Constants.CAN_NOT_SET_MULTIPLE_PRIMARY_MOBILE,
                    customerCreateRequest.getMobiles().toString()));
        }
    }

    private void validateEmail(CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet) {
        log.info("validating email" + " : " + customerCreateRequest.getEmails());
        if (customerCreateRequest.getEmails().isEmpty()) {
            log.error(Constants.BAD_REQUEST + ", " + Constants.ATLEAST_ONE_EMAIL_SHOULD_BE_PRESENT + " : "
                    + customerCreateRequest.getEmails().toString());
            errorMessageSet.add(new ErrorMessage(Constants.ATLEAST_ONE_EMAIL_SHOULD_BE_PRESENT,
                    customerCreateRequest.getEmails().toString()));
        } else {
            checkForMultiplePrimaryEmail(customerCreateRequest, errorMessageSet);
        }
    }

    private void checkForMultiplePrimaryEmail(CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet) {
        int primary = 0;
        for (EmailCreateRequest emailCreateRequest : customerCreateRequest.getEmails()) {
            log.info("checking for multiple primary email" + " : " + customerCreateRequest.getEmails());
            if (emailCreateRequest.isPrimary()) {
                primary++;
            }
        }
        if (1 < primary) {
            log.error(Constants.BAD_REQUEST + ", " + Constants.CAN_NOT_SET_MULTIPLE_PRIMARY_EMAIL + " : "
                    + customerCreateRequest.getEmails().toString());
            errorMessageSet.add(new ErrorMessage(Constants.CAN_NOT_SET_MULTIPLE_PRIMARY_EMAIL,
                    customerCreateRequest.getEmails().toString()));
        }
    }

    private void validateAddress(CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet) {
        log.info("validating address" + " : " + customerCreateRequest.getAddress());
        if (customerCreateRequest.getAddress().isEmpty()) {
            log.error(Constants.BAD_REQUEST + "," + Constants.ATLEAST_ONE_ADDRESS_SHOULD_BE_PRESENT + ":"
                    + customerCreateRequest.getAddress().toString());
            errorMessageSet.add(new ErrorMessage(Constants.ATLEAST_ONE_ADDRESS_SHOULD_BE_PRESENT,
                    customerCreateRequest.getAddress().toString()));
        } else {
            checkForMultipleCurrentAddress(customerCreateRequest, errorMessageSet);
        }
    }

    private void checkForMultipleCurrentAddress(CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet) {
        int current = 0;
        for (AddressCreateRequest addressCreateRequest : customerCreateRequest.getAddress()) {
            log.info("checking for multiple current address" + " : " + customerCreateRequest.getAddress());
            if (AddressType.CURRENT.equals(addressCreateRequest.getAddressType().trim())) {
                current++;
            }
        }
        if (1 < current) {
            log.error(Constants.BAD_REQUEST + ", " + Constants.CAN_NOT_SET_MULTIPLE_CURRENT_ADDRESS + " : "
                    + customerCreateRequest.getAddress().toString());
            errorMessageSet.add(new ErrorMessage(Constants.CAN_NOT_SET_MULTIPLE_CURRENT_ADDRESS,
                    customerCreateRequest.getAddress().toString()));
        }
    }


    private void validateForUniqueCustomer(
            CustomerCreateRequest customerCreateRequest, Set<ErrorMessage> errorMessageSet,
            Map<String, Customer> customerMap) {
        for (EmailCreateRequest emailCreateRequest : customerCreateRequest.getEmails()) {
            Customer customer = customerRepository.findByEmailEmailAddress
                    (emailCreateRequest.getEmailAddress());
            log.info("Validating for unique customer by email ", customer);
            if (null != customer) {
                log.error(Constants.BAD_REQUEST + "," + Constants.RECORD_ALREADY_EXIST + ":"
                        + emailCreateRequest.getEmailAddress());
                errorMessageSet.add(new ErrorMessage(Constants.RECORD_ALREADY_EXIST,
                        emailCreateRequest.getEmailAddress()));
            } else {
                customerMap.put(customerCreateRequest.getFirstName(), customer);
            }
        }
    }

}
