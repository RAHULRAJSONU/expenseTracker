package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.repository.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;

}
