package com.silvertech.expenseTracker.controller;

import com.silvertech.expenseTracker.domain.entity.CustomerProjection;
import com.silvertech.expenseTracker.domain.request.customer.CustomerCreateRequestList;
import com.silvertech.expenseTracker.domain.request.customer.CustomerUpdateRequestList;
import com.silvertech.expenseTracker.domain.resource.assembler.UICategoryProjectionResourceProcessor;
import com.silvertech.expenseTracker.domain.resource.assembler.UICustomerProjectionResourceAssembler;
import com.silvertech.expenseTracker.domain.resource.customer.UICustomerProjectionResource;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.domain.response.customer.CustomerCreateResponse;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.service.CustomerCreateService;
import com.silvertech.expenseTracker.service.CustomerExportService;
import com.silvertech.expenseTracker.service.CustomerUpdateService;
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
import org.springframework.hateoas.Resource;
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
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RepositoryRestController
@Api(value = "Customer controller",
        description = "All supported operations on Customer")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/customers")
@Validated
public class CustomerController {

    private PagedResourcesAssembler<CustomerProjection> customerPagedResourcesAssembler;
    private CustomerCreateService customerCreateService;
    private CustomerUpdateService customerUpdateService;
    private CustomerExportService customerExportService;
    private UICategoryProjectionResourceProcessor uiCategoryProjectionResourceProcessor;
    private UICustomerProjectionResourceAssembler uiCustomerProjectionResourceAssembler;


    @ApiOperation(value = "POST operations on Customer")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PostMapping(produces = {Constants.HAL_JSON_TYPE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createCustomer(
            @ApiParam(name = "Customer Create Request Object",
                    value = "Customer Request", required = true)
            @Valid
            @RequestBody List<CustomerCreateRequestList> customerCreateRequestLists) {

        log.info("CustomerCreate Request {} ", customerCreateRequestLists);

        CustomerCreateResponse createUpdateResponse
                = customerCreateService.createCustomer(customerCreateRequestLists, getUser());

        log.info("CustomerCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.CREATED);
        return new ResponseEntity<CustomerCreateResponse>(createUpdateResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Patch operations on Customer")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PatchMapping(produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateCustomer(
            @ApiParam(name = "Customer Update Request Object",
                    value = "Customer Request", required = true)
            @Valid
            @RequestBody List<CustomerUpdateRequestList> customerUpdateRequestLists) {

        log.info("CustomerCreate Request {} ", customerUpdateRequestLists);

        CreateUpdateResponse createUpdateResponse = customerUpdateService.
                updateCustomer(customerUpdateRequestLists, getUser());


        log.info("CustomerCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.OK);

        return new ResponseEntity<CreateUpdateResponse>(createUpdateResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Customer Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = CustomerProjection.class)})
    @GetMapping
    public ResponseEntity<?> getCustomers(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        log.info("Get Customers");
        Page<CustomerProjection> customerProjections =
                customerExportService.getCustomers(pageable);
        log.info("Get Customers - : " + customerProjections.getContent());
        return generateResponse(customerProjections);
    }

    @ApiOperation(value = "Unique Customer Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = CustomerProjection.class)})
    @GetMapping(name = "guid", value = "{guid}/customer", produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> singleCustomerLookup(
            @Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
                    message = ErrorMessage.ID_ERROR) @PathVariable String guid) {
        log.info("Single Customer Look Up guid::" + guid);
        CustomerProjection customerProjection =
                customerExportService.findOneById(UUID.fromString(guid));
        if (null != customerProjection) {
            return ResponseEntity.ok().body(convertToResource(customerProjection));
        } else {
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.CUSTOMER_NOT_FOUND + guid);
        }
    }


    private ResponseEntity<PagedResources> generateResponse(Page<CustomerProjection> projections) {
        if (projections.hasContent()) {
            return new ResponseEntity(customerPagedResourcesAssembler.
                    toResource(projections),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(customerPagedResourcesAssembler.
                    toEmptyResource(projections, UICustomerProjectionResource.class,
                            null), HttpStatus.OK);
        }
    }

    public Resource<CustomerProjection> convertToResource(CustomerProjection customerProjection) {
        Resource<CustomerProjection> customerProjectionResource =
                new Resource<>(customerProjection);
        return customerProjectionResource;
    }

    private String getUser() {
        return "DEVELOPER";
    }

}
