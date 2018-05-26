package com.silvertech.expenseTracker.validator;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.request.CategoryCreateRequest;
import com.silvertech.expenseTracker.domain.request.CategoryCreateRequestList;
import com.silvertech.expenseTracker.domain.request.CategoryUpdateRequest;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorCodeValidationException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.repository.CategoryRepository;
import com.silvertech.expenseTracker.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Component
@Slf4j
public class CategoryValidator {
    @Autowired
    private CategoryRepository categoryRepository;

    public void validateCategoryUpdateRequest(
            CategoryUpdateRequest categoryUpdateRequest) {
        if (categoryUpdateRequest.getDescription() == null &&
                categoryUpdateRequest.getName() == null &&
                categoryUpdateRequest.getActive() == null) {
            throw new ErrorCodeException(HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_UPDATE);
        }
    }

    public Map<String, Category> validateCategoryRequest(
            List<CategoryCreateRequestList> categoryCreateRequestListList) {
        if (CollectionUtils.isEmpty(categoryCreateRequestListList)) {
            throw new ErrorCodeException
                    (HttpStatus.BAD_REQUEST, Constants.BAD_REQUEST_FOR_CREATE);
        } else {
            return validateCategoryCreateRequest(categoryCreateRequestListList);
        }
    }

    private Map<String, Category> validateCategoryCreateRequest(
            List<CategoryCreateRequestList> categoryCreateRequestLists) {
        Map<String, Category> categoryMap = new HashMap<String, Category>();
        Set<ErrorMessage> errorMessageSet = new HashSet();
        for (CategoryCreateRequestList categoryCreateRequestList :
                categoryCreateRequestLists) {
            validateCategoryRequestList(categoryCreateRequestList, errorMessageSet, categoryMap);
        }

        if (errorMessageSet.isEmpty()) {
            return categoryMap;
        } else {
            List<ErrorMessage> errorMessageList = new ArrayList<>();
            errorMessageList.addAll(errorMessageSet);
            throw new ErrorCodeValidationException(HttpStatus.BAD_REQUEST, errorMessageList);
        }
    }

    private void validateCategoryRequestList(CategoryCreateRequestList catagoryCreateRequestList,
                                             Set<ErrorMessage> errorMessageSet,
                                             Map<String, Category> categoryHashMap) {
        for (CategoryCreateRequest categoryCreateRequest
                : catagoryCreateRequestList.getCategoryCreateRequests()) {
            if (null != categoryCreateRequest.getParent()) {
                Category category = categoryRepository.getOne(UUID.fromString(categoryCreateRequest.getParent()));
                log.info("getting category for parent id- " + category);
                if (category == null) {
                    log.error(Constants.BAD_REQUEST + "," + Constants.PARENT_CATEGORY_DOES_NOT_EXISTS + ":"
                            + categoryCreateRequest.getParent());
                    errorMessageSet.add(new ErrorMessage(Constants.PARENT_CATEGORY_DOES_NOT_EXISTS,
                            categoryCreateRequest.getParent()));
                } else {
                    categoryHashMap.put(categoryCreateRequest.getParent(), category);
                    validateForUniqueCategory(
                            categoryCreateRequest, errorMessageSet, categoryHashMap);
                }
            } else {
                validateForUniqueCategory(
                        categoryCreateRequest, errorMessageSet, categoryHashMap);
            }
        }
    }

    private void validateForUniqueCategory(
            CategoryCreateRequest categoryCreateRequest, Set<ErrorMessage> errorMessageSet,
            Map<String, Category> categoryHashMap) {
        Category category = categoryRepository.getByName(categoryCreateRequest.getName());
        if (category != null) {
            log.error(Constants.BAD_REQUEST + "," + Constants.RECORD_ALREADY_EXIST + ":"
                    + categoryCreateRequest.getName());
            errorMessageSet.add(new ErrorMessage(Constants.RECORD_ALREADY_EXIST,
                    categoryCreateRequest.getName()));
        } else {
            categoryHashMap.put(categoryCreateRequest.getName(), category);
        }
    }

}
