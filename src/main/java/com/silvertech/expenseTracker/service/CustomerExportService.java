package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.entity.CustomerProjection;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CustomerExportService {

    CustomerRepository customerRepository;

    CustomerCommonService customerCommonService;

    /*public Page<CustomerProjection> getSubCategories(UUID categoryId, Pageable pageable) {
        Category category = customerCommonService.getCategoryById(categoryId);
        if (category == null) {
            log.error("Parent Category not found for categoryId: " + categoryId);
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.CATEGORY_NOT_FOUND + categoryId);
        } else {
            return customerRepository
                    .findByParent(category, pageable);
        }
    }*/

    public Page<CustomerProjection> getCustomers(Pageable pageable) {
        return customerRepository
                .findAllProjectedBy(pageable);
    }

    public Optional<Customer> getCustomerById(UUID guid) {
        return customerRepository.findById(guid);
    }

    public CustomerProjection findOneById(UUID uuid) {
        return customerRepository.findOneById(uuid);
    }
}
