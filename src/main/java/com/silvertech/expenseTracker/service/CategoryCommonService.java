package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.repository.CategoryRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CategoryCommonService {

    CategoryRepository categoryRepository;

    public Category getCategoryById(UUID categoryId) {
        log.info("Find Category by Id " + categoryId);
        Category category = categoryRepository.getOne(categoryId);
        log.info("getCategoryById " + category);
        if (null == category) {
            log.error("Category not Found");
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.CATEGORY_NOT_FOUND);
        }
        log.info("Output getCategoryById {} ", categoryId);
        return category;
    }

}
