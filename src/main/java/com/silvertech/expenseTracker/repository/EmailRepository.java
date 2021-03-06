package com.silvertech.expenseTracker.repository;

import com.silvertech.expenseTracker.domain.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface EmailRepository extends JpaRepository<Email, UUID> {
}
