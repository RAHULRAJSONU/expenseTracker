package com.silvertech.expenseTracker.repository;

import com.silvertech.expenseTracker.domain.entity.Customer;
import com.silvertech.expenseTracker.domain.entity.CustomerProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Page<CustomerProjection> findAllProjectedBy(Pageable pageable);

    Customer getById(UUID id);

    CustomerProjection getByEmailEmailAddress(String emailAddress);

    Customer findByEmailEmailAddress(String emailAddress);

    CustomerProjection findOneById(UUID uuid);
}
