package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Address;
import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.entity.Email;
import com.silvertech.expenseTracker.domain.entity.Gender;
import com.silvertech.expenseTracker.domain.entity.Mobile;
import com.silvertech.expenseTracker.domain.request.customer.AddressCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.CustomerUICreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.EmailCreateRequest;
import com.silvertech.expenseTracker.domain.request.customer.MobileCreateRequest;
import com.silvertech.expenseTracker.domain.resource.customer.CustomerCreateResponseBuilder;
import com.silvertech.expenseTracker.domain.response.customer.CustomerResponse;
import com.silvertech.expenseTracker.mapper.CustomerCreateMapper;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CustomerService {

    private CustomerRepository customerRepository;
    private CustomerCommonService customerCommonService;
    private CustomerCreateMapper customerCreateMapper;
    private CustomerCreateResponseBuilder customerCreateResponseBuilder;
    private GenderService genderService;

    public Customer callCreateCustomerService(CustomerCreateRequest customerCreateRequest,
                                              String userId) {
        log.info("Input callCreateCustomerService {} ", customerCreateRequest);
        Customer customer
                = createCustomer(customerCreateMapper.createCustomerCreateRequest
                (customerCreateRequest, userId));
        log.info("Output callCreateCategoryService {} ", customer);
        return customer;
    }

    private Customer createCustomer(CustomerUICreateRequest customerUICreateRequest
    ) {

        log.info("Customer create request {}", customerUICreateRequest);
        Customer customer = constructCustomer(customerUICreateRequest);
        return customer;
    }

    private Customer constructCustomer(CustomerUICreateRequest customerUICreateRequest)

    {
        Customer customer = new Customer();
        customer.setFirstName(customerUICreateRequest.getFirstName().toUpperCase());
        if (null != customerUICreateRequest.getMiddleName()) {
            customer.setMiddleName(customerUICreateRequest.getMiddleName().toUpperCase());
        }
        customer.setLastName(customerUICreateRequest.getLastName().toUpperCase());
        customer.setDob(customerUICreateRequest.getDob());
        Gender gender = genderService.getById(customerUICreateRequest.getGender());
        customer.setGender(gender);
        customer.setEmail(constructEmail(customerUICreateRequest.getEmailList()));
        customer.setAddress(constructAddress(customerUICreateRequest.getAddressList()));
        customer.setMobile(constructMobile(customerUICreateRequest.getMobileList()));
        customer.setLastModifiedUser(customerUICreateRequest.getUser());
        customer.setCreatedDttm(new DateFormatUtil().getCurrentPstDate());
        customer.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());

        return customer;
    }

    private List<Mobile> constructMobile(List<MobileCreateRequest> mobileList) {
        List<Mobile> mobiles = new ArrayList<>();
        for (MobileCreateRequest mobileCreateRequest : mobileList) {
            Mobile mobile = new Mobile();
            mobile.setCountryCode(mobileCreateRequest.getCountryCode());
            mobile.setMobileNumber(mobileCreateRequest.getMobileNumber());
            mobile.setPrimary(mobileCreateRequest.isPrimary());
            mobile.setCreatedDttm(new DateFormatUtil().getCurrentPstDate());
            mobile.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());
            mobiles.add(mobile);
        }
        return mobiles;
    }

    private List<Address> constructAddress(List<AddressCreateRequest> addressCreateRequests) {
        List<Address> addresses = new ArrayList<>();
        for (AddressCreateRequest addressCreateRequest : addressCreateRequests) {
            Address address = new Address();
            address.setAddressType(addressCreateRequest.getAddressType());
            address.setCountry(addressCreateRequest.getCountry());
            address.setState(addressCreateRequest.getState());
            address.setCity(addressCreateRequest.getCity());
            address.setStreetAddress(addressCreateRequest.getStreetAddress());
            address.setZip(addressCreateRequest.getZip());
            address.setCreatedDttm(new DateFormatUtil().getCurrentPstDate());
            address.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());
            addresses.add(address);
        }
        return addresses;
    }

    private List<Email> constructEmail(List<EmailCreateRequest> emailList) {
        List<Email> emails = new ArrayList<>();
        for (EmailCreateRequest emailCreateRequest : emailList) {
            Email email = new Email();
            email.setEmailAddress(emailCreateRequest.getEmailAddress());
            email.setPrimary(emailCreateRequest.isPrimary());
            email.setCreatedDttm(new DateFormatUtil().getCurrentPstDate());
            email.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());
            emails.add(email);
        }
        return emails;
    }

    public List<Customer> saveAllCustomer(List<Customer> customerList) {
        return customerRepository.saveAll(customerList);
    }

    public boolean buildCustomerResponses(List<Customer> responseCustomerList, List<CustomerResponse> customerResponses) {
        Boolean customerResponse;
        for (Customer responseCategory : responseCustomerList) {
            customerResponses.add(customerCreateResponseBuilder
                    .createCustomerResponse(responseCategory));
        }
        customerResponse = true;
        return customerResponse;
    }
}
