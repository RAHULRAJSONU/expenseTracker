package com.silvertech.expenseTracker.service;


import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.request.CategoryCreateRequest;
import com.silvertech.expenseTracker.domain.request.CategoryUICreateRequest;
import com.silvertech.expenseTracker.domain.request.CategoryUIUpdateRequest;
import com.silvertech.expenseTracker.domain.resource.CategoryCreateResponseBuilder;
import com.silvertech.expenseTracker.domain.response.CategoryResponse;
import com.silvertech.expenseTracker.mapper.CategoryCreateMapper;
import com.silvertech.expenseTracker.repository.CategoryRepository;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryCommonService categoryCommonService;
    private CategoryCreateMapper categoryCreateMapper;
    private CategoryCreateResponseBuilder categoryCreateResponseBuilder;

    public Category callCreateCategoryService(CategoryCreateRequest categoryCreateRequest,
                                              Category parentCategory,
                                              String userId) {
        log.info("Input callCreateCategoryService {} ", categoryCreateRequest);
        Category category
                = createCategory(categoryCreateMapper.createCategoryCreateRequest
                (categoryCreateRequest, userId), parentCategory);
        log.info("Output callCreateCategoryService {} ", category);
        return category;
    }

    private Category createCategory(CategoryUICreateRequest categoryUICreateRequest,
                                    Category parentCategory) {

        log.info("Category create request {}", categoryUICreateRequest);
        Category category = constructCategory(parentCategory, categoryUICreateRequest);
        return category;
    }

    public List<Category> saveAllCategory(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    public Boolean buildCategoryResponses(List<Category> categories,
                                          List<CategoryResponse> categoryResponses) {
        Boolean categoryResponse;
        for (Category responseCategory : categories) {
            categoryResponses.add(categoryCreateResponseBuilder
                    .createCaategoryResponse(responseCategory));
        }
        categoryResponse = true;
        return categoryResponse;
    }

    public UUID updateCategory(CategoryUIUpdateRequest categoryUpdateRequest) {

        log.info("UpdateCategory request received{} ", categoryUpdateRequest);
        UUID categoryId = categoryUpdateRequest.getId();
        Category category = categoryCommonService.getCategoryById(categoryId);

        if (validateCategoryUpdate(category, categoryUpdateRequest)) {
            log.info(" validateCategoryUpdate returned true:No Updates " +
                    "found for category table");
            return category.getId();
        }

        if (!validateCategoryDescriptionUpdate(category,
                categoryUpdateRequest)) {
            category.setDescription(categoryUpdateRequest
                    .getDescription()
                    .toUpperCase());
        }

        if (!validateCategoryActiveUpdate
                (category, categoryUpdateRequest)) {
            category.setActive(categoryUpdateRequest.getActive());
        }
        if (categoryUpdateRequest.getUser() != null) {
            category.setLastModifiedUser(categoryUpdateRequest.getUser());
        }
        category.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());

        Category updatedCategory = categoryRepository.save(category);

        log.info("Output updateCategory Id {} ", updatedCategory.getId());
        return updatedCategory.getId();
    }

    private boolean validateCategoryUpdate(Category category,
                                           CategoryUIUpdateRequest categoryUIUpdateRequest) {
        boolean noDescriptionUpdate = validateCategoryDescriptionUpdate(category,
                categoryUIUpdateRequest);

        boolean noActiveUpdate = validateCategoryActiveUpdate
                (category, categoryUIUpdateRequest);

        if (noDescriptionUpdate && noActiveUpdate) {
            return true;
        }
        return false;

    }

    private boolean validateCategoryActiveUpdate
            (Category category,
             CategoryUIUpdateRequest categoryUpdateRequest) {
        boolean noActiveUpdate = false;
        if (categoryUpdateRequest.getActive() == null) {
            noActiveUpdate = true;
        } else if (category.isActive() == categoryUpdateRequest.getActive()) {
            noActiveUpdate = true;

        }


        return noActiveUpdate;
    }

    private boolean validateCategoryDescriptionUpdate(Category category,
                                                      CategoryUIUpdateRequest
                                                              categoryUICreateRequest) {
        boolean noDescriptionUpdate = false;
        if (categoryUICreateRequest.getDescription() == null) {
            noDescriptionUpdate = true;
        } else if (category.getDescription()
                .equalsIgnoreCase(categoryUICreateRequest.getDescription())) {
            noDescriptionUpdate = true;

        }

        return noDescriptionUpdate;
    }

    private Category constructCategory(Category parentCategory,
                                       CategoryUICreateRequest categoryUICreateRequest)

    {
        Category category = new Category();
        category.setName(categoryUICreateRequest.getName().toUpperCase());
        category.setDescription(categoryUICreateRequest.getDescription().toUpperCase());
        category.setActive(categoryUICreateRequest.getActive());
        category.setParent(parentCategory);
        category.setLastModifiedUser(categoryUICreateRequest.getUser());
        category.setCreatedDttm(new DateFormatUtil().getCurrentPstDate());
        category.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());

        return category;
    }

}
