package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.repository.CategoryRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CategoryExportService {

    CategoryRepository categoryRepository;

    CategoryCommonService categoryCommonService;

    public Page<CategoryProjection> getSubCategories(UUID categoryId, Pageable pageable) {
        Category category = categoryCommonService.getCategoryById(categoryId);
        if (category == null) {
            log.error("Parent Category not found for categoryId: " + categoryId);
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.CATEGORY_NOT_FOUND + categoryId);
        } else {
            return categoryRepository
                    .findByParent(category, pageable);
        }
    }

    public Page<CategoryProjection> getCategories(Pageable pageable) {
        return categoryRepository
                .findAllProjectedBy(pageable);
    }

    public Optional<Category> getCategoryById(UUID guid) {
        return categoryRepository.findById(guid);
    }
}
