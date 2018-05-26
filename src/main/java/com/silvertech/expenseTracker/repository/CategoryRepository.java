package com.silvertech.expenseTracker.repository;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(excerptProjection = CategoryProjection.class)
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Page<CategoryProjection> findAllProjectedBy(Pageable pageable);

    Category getById(UUID id);

    CategoryProjection findByName(String name);

    Page<CategoryProjection> findByParent(Category parent, Pageable pageable);

    Category getByName(String name);

}
