package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.CustomerProjection;
import com.silvertech.expenseTracker.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerGetService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CustomerRepository customerRepository;

    public Page<CustomerProjection> getAll(Pageable pageable) {
        return customerRepository.findAllProjectedBy(pageable);
    }

}
