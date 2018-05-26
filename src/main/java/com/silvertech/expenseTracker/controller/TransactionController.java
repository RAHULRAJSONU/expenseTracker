package com.silvertech.expenseTracker.controller;

import com.silvertech.expenseTracker.domain.entity.Transaction;
import com.silvertech.expenseTracker.domain.entity.TransactionProjection;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionCreateRequestList;
import com.silvertech.expenseTracker.domain.request.transaction.TransactionUpdateRequestList;
import com.silvertech.expenseTracker.domain.resource.transaction.UITransactionProjectionResource;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.domain.response.transaction.TransactionCreateResponse;
import com.silvertech.expenseTracker.domain.specification.transaction.UserSpecificationsBuilder;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.service.TransactionCreateService;
import com.silvertech.expenseTracker.service.TransactionService;
import com.silvertech.expenseTracker.service.TransactionUpdateService;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RepositoryRestController
@Api(value = "Transaction controller",
        description = "All supported operations on Transaction")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/transactions")
@Validated
public class TransactionController {
    private TransactionCreateService transactionCreateService;
    private TransactionUpdateService transactionUpdateService;
    private TransactionService transactionService;
    private PagedResourcesAssembler<TransactionProjection> transactionProjectionPagedResourcesAssembler;

    @ApiOperation(value = "POST operations on Transaction")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PostMapping(produces = {Constants.HAL_JSON_TYPE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createTransaction(
            @ApiParam(name = "Transaction Create Request Object",
                    value = "Transaction Request", required = true)
            @Valid
            @RequestBody List<TransactionCreateRequestList> transactionCreateRequestLists) {

        log.info("TransactionCreate Request {} ", transactionCreateRequestLists);

        TransactionCreateResponse createUpdateResponse
                = transactionCreateService.createTransaction(transactionCreateRequestLists, getUser());

        log.info("TransactionCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.CREATED);
        return new ResponseEntity<TransactionCreateResponse>(createUpdateResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Patch operations on Transaction")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PatchMapping(produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateTransaction(
            @ApiParam(name = "Transaction Update Request Object",
                    value = "Transaction Request", required = true)
            @Valid
            @RequestBody List<TransactionUpdateRequestList> transactionUpdateRequestLists) {

        log.info("TransactionCreate Request {} ", transactionUpdateRequestLists);

        CreateUpdateResponse createUpdateResponse = transactionUpdateService.
                updateTransaction(transactionUpdateRequestLists, getUser());


        log.info("TransactionCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.OK);

        return new ResponseEntity<CreateUpdateResponse>(createUpdateResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Transaction Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = TransactionProjection.class)})
    @GetMapping
    public ResponseEntity<?> getCustomers(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        log.info("Get Transaction");
        Page<TransactionProjection> transactionProjections =
                transactionService.getTransaction(pageable);
        log.info("Get Transaction - : " + transactionProjections.getContent());
        return generateResponse(transactionProjections);
    }

    @ApiOperation(value = "Unique Transaction Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = TransactionProjection.class)})
    @GetMapping(name = "guid", value = "{guid}/transaction", produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> singleTransactionLookup(
            @Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
                    message = ErrorMessage.ID_ERROR) @PathVariable String guid) {
        log.info("Single Transaction Look Up guid::" + guid);
        TransactionProjection transactionProjection =
                transactionService.findOneById(UUID.fromString(guid));
        if (null != transactionProjection) {
            return ResponseEntity.ok().body(convertToResource(transactionProjection));
        } else {
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.TRANSACTION_NOT_FOUND + guid);
        }
    }

    @ApiOperation(value = "Transaction by account id look up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = TransactionProjection.class)})
    @GetMapping(name = "guid", value = "account/{guid}/transaction")
    public ResponseEntity<?> transactionByAccount(@Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
            message = ErrorMessage.ID_ERROR) @PathVariable String guid,
                                                  @PageableDefault(page = 0, size = 20) Pageable pageable) {
        log.info("transactions look up guid : " + guid);
        Page<TransactionProjection> transactionProjection = transactionService.getByAccountId(UUID.fromString(guid), pageable);
        return generateResponse(transactionProjection);
    }

    @ApiOperation(value = "Transaction by category id look up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = TransactionProjection.class)})
    @GetMapping(name = "guid", value = "category/{guid}/transaction")
    public ResponseEntity<?> transactionByCategory(@Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
            message = ErrorMessage.ID_ERROR) @PathVariable String guid,
                                                   @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        log.info("Transaction by category lookup guid : " + guid);
        Page<TransactionProjection> transactionProjections = transactionService.getByCategoryId(guid, pageable);
        return generateResponse(transactionProjections);
    }

    @ApiOperation(value = "Transaction Search")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = Transaction.class)})
    @RequestMapping(method = RequestMethod.GET, value = "/transaction-search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam(value = "search") String search,
                                    @PageableDefault(page = 0, size = 20) Pageable pageable) {
        UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Transaction> spec = builder.build();
        //List<Transaction> transactionProjections = transactionService.findAll(spec);
        Page<TransactionProjection> transactionProjections = transactionService
                .findAll(spec, TransactionProjection.class, pageable);
        return generateResponse(transactionProjections);
    }

    private ResponseEntity<PagedResources> generateResponse(Page<TransactionProjection> projections) {
        if (projections.hasContent()) {
            return new ResponseEntity(transactionProjectionPagedResourcesAssembler.
                    toResource(projections),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(transactionProjectionPagedResourcesAssembler.
                    toEmptyResource(projections, UITransactionProjectionResource.class,
                            null), HttpStatus.OK);
        }
    }

    public Resource<TransactionProjection> convertToResource(TransactionProjection transactionProjection) {
        Resource<TransactionProjection> transactionProjectionResource =
                new Resource<>(transactionProjection);
        return transactionProjectionResource;
    }

    private String getUser() {
        return "DEVELOPER";
    }
}
