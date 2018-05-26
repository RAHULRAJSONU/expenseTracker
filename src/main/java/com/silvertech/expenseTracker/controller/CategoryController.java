package com.silvertech.expenseTracker.controller;

import com.silvertech.expenseTracker.domain.entity.Category;
import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import com.silvertech.expenseTracker.domain.request.CategoryCreateRequestList;
import com.silvertech.expenseTracker.domain.request.CategoryUpdateRequestList;
import com.silvertech.expenseTracker.domain.resource.UICategoryProjectionResource;
import com.silvertech.expenseTracker.domain.resource.assembler.UICategoryProjectionResourceAssembler;
import com.silvertech.expenseTracker.domain.resource.assembler.UICategoryProjectionResourceProcessor;
import com.silvertech.expenseTracker.domain.response.CategoryCreateResponse;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.service.CategoryCreateService;
import com.silvertech.expenseTracker.service.CategoryExportService;
import com.silvertech.expenseTracker.service.CategoryGetService;
import com.silvertech.expenseTracker.service.CategoryUpdateService;
import com.silvertech.expenseTracker.utils.Constants;
import com.silvertech.expenseTracker.validator.Validation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RepositoryRestController
@Api(value = "Category controller",
        description = "All supported operations on Category")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private PagedResourcesAssembler<CategoryProjection> categoryPagedResourcesAssembler;
    private CategoryGetService categoryService;
    private CategoryCreateService categoryCreateService;
    private CategoryUpdateService categoryUpdateService;
    private CategoryExportService categoryExportService;
    private UICategoryProjectionResourceProcessor UICategoryProjectionResourceProcessor;
    private UICategoryProjectionResourceAssembler uiCategoryProjectionResourceAssembler;


    @ApiOperation(value = "POST operations on Category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PostMapping(produces = {Constants.HAL_JSON_TYPE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createCategory(
            @ApiParam(name = "Category Create Request Object",
                    value = "Category Request", required = true)
            @Valid
            @RequestBody List<CategoryCreateRequestList> categories) {

        log.info("CategoryCreate Request {} ", categories);

        CategoryCreateResponse createUpdateResponse
                = categoryCreateService.createCategory(categories, getUser());

        log.info("CategoryCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.CREATED);
        return new ResponseEntity<CategoryCreateResponse>(createUpdateResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Patch operations on Category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PatchMapping(produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateCategory(
            @ApiParam(name = "Category Update Request Object",
                    value = "Category Request", required = true)
            @Valid
            @RequestBody List<CategoryUpdateRequestList> categoryUpdateRequestLists) {

        log.info("CategoryCreate Request {} ", categoryUpdateRequestLists);

        CreateUpdateResponse createUpdateResponse = categoryUpdateService.
                updateCategory(categoryUpdateRequestLists, getUser());


        log.info("CategoryCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.OK);

        return new ResponseEntity<CreateUpdateResponse>(createUpdateResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Sub-Category Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = CategoryProjection.class)})
    @GetMapping(value = "{guid}/all-children-categories")
    public ResponseEntity<?> getSubCategories(@Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
            message = ErrorMessage.ID_ERROR)
                                              @PathVariable String guid,
                                              @PageableDefault(page = 0, size = 20) Pageable pageable) {
        log.info("Get SubCategories By id::" + guid);
        Page<CategoryProjection> categoryProjections =
                categoryExportService.getSubCategories(UUID.fromString(guid), pageable);
        log.info("Get SubCategories - : " + categoryProjections.getContent());
        return generateResponse(categoryProjections);
    }

    @ApiOperation(value = "Categories Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = CategoryProjection.class)})
    @GetMapping
    public ResponseEntity<?> getCategories(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        log.info("Get Categories");
        Page<CategoryProjection> categoryProjections =
                categoryExportService.getCategories(pageable);
        log.info("Get SubCategories - : " + categoryProjections.getContent());
        return generateResponse(categoryProjections);
    }

    @ApiOperation(value = "Category Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = CategoryProjection.class)})
    @GetMapping(name = "guid", value = "{guid}/parent-category", produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> singleCategoryLookup(
            @Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
                    message = ErrorMessage.ID_ERROR) @PathVariable String guid) {
        log.info("Single Category Look Up guid::" + guid);
        Optional<Category> categoryOptional =
                categoryExportService.getCategoryById(UUID.fromString(guid));
        if (categoryOptional.isPresent()) {
            return ResponseEntity.ok().body(categoryOptional.get());
        } else {
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.CATEGORY_NOT_FOUND + guid);
        }
    }


    private ResponseEntity<PagedResources> generateResponse(Page<CategoryProjection> projections) {
        if (projections.hasContent()) {
            return new ResponseEntity(categoryPagedResourcesAssembler.
                    toResource(projections),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(categoryPagedResourcesAssembler.
                    toEmptyResource(projections, UICategoryProjectionResource.class,
                            null), HttpStatus.OK);
        }
    }

    private String getUser() {
        return "DEVELOPER";
    }


}
