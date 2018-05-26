package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.repository.MobileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MobileService {
    @Autowired
    private MobileRepository mobileRepository;

}
