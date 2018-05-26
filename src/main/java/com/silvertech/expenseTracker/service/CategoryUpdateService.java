package com.silvertech.expenseTracker.service;


import com.silvertech.expenseTracker.domain.request.CategoryUIUpdateRequest;
import com.silvertech.expenseTracker.domain.request.CategoryUpdateRequest;
import com.silvertech.expenseTracker.domain.request.CategoryUpdateRequestList;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.validator.CategoryValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class CategoryUpdateService {

    private CategoryValidator categoryValidator;

    private CategoryService categoryService;

    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public CreateUpdateResponse updateCategory
            (List<CategoryUpdateRequestList> categoryUpdateRequests, String userId) {
        List<UUID> updateCategoryIds = new ArrayList<>();
        log.info("category update request {} "
                , categoryUpdateRequests);
        for (CategoryUpdateRequestList categoryUpdateRequestList : categoryUpdateRequests) {

            for (CategoryUpdateRequest categoryUpdateRequest
                    : categoryUpdateRequestList.getCategoryUpdateRequests()) {
                categoryValidator.validateCategoryUpdateRequest(categoryUpdateRequest);

                UUID updateCategoryId = categoryService.updateCategory(
                        buildCategoryUpdateRequest(categoryUpdateRequest, userId));

                updateCategoryIds.add(updateCategoryId);
            }
        }
        log.info("Output updateCategory HTTP Status code {} ",
                HttpStatus.OK.value());
        /*applicationEventPublisher.publishEvent(new AfterSaveEvent(CategoryUpdateEvent.builder()
                .updateCategoryIds(updateCategoryIds).build()));*/
        return new CreateUpdateResponse(HttpStatus.OK.value());
    }

    private CategoryUIUpdateRequest buildCategoryUpdateRequest(
            CategoryUpdateRequest categoryUpdateRequest, String userId) {

        CategoryUIUpdateRequest categoryUIUpdateRequest = new CategoryUIUpdateRequest();
        categoryUIUpdateRequest.setId(UUID.fromString(categoryUpdateRequest.getId()));
        categoryUIUpdateRequest.setActive(categoryUpdateRequest.getActive());
        categoryUIUpdateRequest.setDescription(categoryUpdateRequest.getDescription());
        categoryUIUpdateRequest.setUser(userId);
        return categoryUIUpdateRequest;
    }
}
