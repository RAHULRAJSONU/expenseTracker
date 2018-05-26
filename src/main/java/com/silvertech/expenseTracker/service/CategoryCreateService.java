package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.request.CategoryCreateRequest;
import com.silvertech.expenseTracker.domain.request.CategoryCreateRequestList;
import com.silvertech.expenseTracker.domain.response.CategoryCreateResponse;
import com.silvertech.expenseTracker.domain.response.CategoryResponse;
import com.silvertech.expenseTracker.domain.response.CategoryResponseList;
import com.silvertech.expenseTracker.mapper.CategoryCreateMapper;
import com.silvertech.expenseTracker.repository.CategoryRepository;
import com.silvertech.expenseTracker.validator.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CategoryCreateService {

    List<CategoryResponse> categoryResponses;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryValidator categoryValidator;
    @Autowired
    private CategoryCreateMapper categoryCreateMapper;
    @Autowired
    private CategoryService categoryService;

    @Transactional
    public CategoryCreateResponse createCategory
            (List<CategoryCreateRequestList> categoryCreateRequestLists, String userId) {
        List<CategoryResponseList> categoryResponseLists = new ArrayList<>();
        log.info("Input createCategory {} ", categoryCreateRequestLists);
        Map<String, Category> categoryHashMap = categoryValidator.
                validateCategoryRequest(categoryCreateRequestLists);
        for (CategoryCreateRequestList categoryCreateRequestList : categoryCreateRequestLists) {
            CategoryResponseList categoryResponseList = new CategoryResponseList();
            categoryResponses = new ArrayList<CategoryResponse>();
            log.info("Create Request for Category " + categoryCreateRequestList);
            createNewCategory(categoryCreateRequestList, categoryHashMap, userId);
            categoryResponseList.setCategoryResponses(categoryResponses);
            categoryResponseLists.add(categoryResponseList);
        }
        log.info("Output createCategory HTTP Response {} ", HttpStatus.CREATED.value());
        /*applicationEventPublisher.publishEvent(new AfterCreateEvent(CategoryCreateEvent.builder()
                .categoryCreateResponse(genderResponseLists).build()));*/
        return new CategoryCreateResponse(HttpStatus.CREATED.value(), categoryResponseLists);
    }

    private void createNewCategory(CategoryCreateRequestList categoryCreateRequestList,
                                   Map<String, Category> categoryMap, String userId) {

        List<Category> categoryList = new ArrayList<>();

        List<Category> responseCategoryList;
        for (CategoryCreateRequest categoryCreateRequest1
                : categoryCreateRequestList.getCategoryCreateRequests()) {
            Category parentCategory = categoryMap.get(categoryCreateRequest1.getParent());
            Category category = categoryService.callCreateCategoryService(
                    categoryCreateRequest1, parentCategory, userId);
            categoryList.add(category);
        }
        responseCategoryList = categoryService.saveAllCategory(categoryList);
        categoryService.buildCategoryResponses(responseCategoryList, categoryResponses);
    }
}
