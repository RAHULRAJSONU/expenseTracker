package com.silvertech.expenseTracker.repository;

import com.silvertech.expenseTracker.annotation.SequenceNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceNumberRepository extends JpaRepository<SequenceNumber, String> {
}
