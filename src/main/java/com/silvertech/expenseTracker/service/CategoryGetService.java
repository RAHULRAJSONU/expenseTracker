package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import com.silvertech.expenseTracker.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryGetService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<CategoryProjection> getAll(Pageable pageable) {
        return categoryRepository.findAllProjectedBy(pageable);
    }

    public Page<CategoryProjection> getByParent(Category parent, Pageable pageable) {
        return categoryRepository.findByParent(parent, pageable);
    }
}
