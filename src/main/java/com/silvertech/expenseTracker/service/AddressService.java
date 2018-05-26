package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

}
