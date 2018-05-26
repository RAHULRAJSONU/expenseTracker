package com.silvertech.expenseTracker.controller;

import com.silvertech.expenseTracker.domain.entity.Account;
import com.silvertech.expenseTracker.domain.entity.AccountProjection;
import com.silvertech.expenseTracker.domain.request.account.AccountCreateRequestList;
import com.silvertech.expenseTracker.domain.request.account.AccountUpdateRequestList;
import com.silvertech.expenseTracker.domain.resource.account.UIAccountProjectionResource;
import com.silvertech.expenseTracker.domain.resource.account.UIAccountProjectionResourceProcessor;
import com.silvertech.expenseTracker.domain.resource.assembler.UIAcccountProjectionResourceAssembler;
import com.silvertech.expenseTracker.domain.response.CreateUpdateResponse;
import com.silvertech.expenseTracker.domain.response.account.AccountCreateResponse;
import com.silvertech.expenseTracker.exception.ErrorCodeException;
import com.silvertech.expenseTracker.exception.ErrorMessage;
import com.silvertech.expenseTracker.service.AccountCreateService;
import com.silvertech.expenseTracker.service.AccountExportService;
import com.silvertech.expenseTracker.service.AccountGetService;
import com.silvertech.expenseTracker.service.AccountUpdateService;
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
@Api(value = "Account Controller", description = "All Supported Operation On Account.")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/accounts")
@Validated
public class AccountController {

    private PagedResourcesAssembler<AccountProjection> accountPagedResourcesAssembler;
    private AccountGetService accountGetService;
    private AccountCreateService accountCreateService;
    private AccountUpdateService accountUpdateService;
    private AccountExportService accountExportService;
    private UIAccountProjectionResourceProcessor uiAccountProjectionResourceProcessor;
    private UIAcccountProjectionResourceAssembler uiAcccountProjectionResourceAssembler;


    @ApiOperation(value = "POST operations on Account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PostMapping(produces = {Constants.HAL_JSON_TYPE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAccount(
            @ApiParam(name = "Account Create Request Object",
                    value = "Account Request", required = true)
            @Valid
            @RequestBody List<AccountCreateRequestList> account) {

        log.info("AccountCreate Request {} ", account);

        AccountCreateResponse createUpdateResponse
                = accountCreateService.createAccount(account, getUser());

        log.info("AccountCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.CREATED);
        return new ResponseEntity<AccountCreateResponse>(createUpdateResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Patch operations on Account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PatchMapping(produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateAccount(
            @ApiParam(name = "Account Update Request Object",
                    value = "Account Request", required = true)
            @Valid
            @RequestBody List<AccountUpdateRequestList> accountUpdateRequestLists) {

        log.info("AccountUpdate Request {} ", accountUpdateRequestLists);

        CreateUpdateResponse createUpdateResponse = accountUpdateService.
                updateAccount(accountUpdateRequestLists, getUser());


        log.info("AccountUpdate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.OK);

        return new ResponseEntity<CreateUpdateResponse>(createUpdateResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Sub-Account Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = AccountProjection.class)})
    @GetMapping(value = "{guid}/all-children-accounts")
    public ResponseEntity<?> getSubAccounts(@Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
            message = ErrorMessage.ID_ERROR)
                                            @PathVariable String guid,
                                            @PageableDefault(page = 0, size = 20) Pageable pageable) {
        log.info("Get SubAccounts By id::" + guid);
        Page<AccountProjection> accountProjections =
                accountExportService.getSubAccount(UUID.fromString(guid), pageable);
        log.info("Get SubAccounts - : " + accountProjections.getContent());
        return generateResponse(accountProjections);
    }

    @ApiOperation(value = "Accounts Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = AccountProjection.class)})
    @GetMapping
    public ResponseEntity<?> getAccounts(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        log.info("Get Accounts");
        Page<AccountProjection> accountProjections =
                accountExportService.getAccounts(pageable);
        log.info("Get Accountss - : " + accountProjections.getContent());
        return generateResponse(accountProjections);
    }

    @ApiOperation(value = "Account Look Up")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS",
            response = AccountProjection.class)})
    @GetMapping(name = "guid", value = "{guid}/parent-account", produces = {Constants.HAL_JSON_TYPE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> singleAcountLookup(
            @Pattern(regexp = Validation.GUID_REGULAR_EXPRESSION,
                    message = ErrorMessage.ID_ERROR) @PathVariable String guid) {
        log.info("Single Account Look Up guid::" + guid);
        Optional<Account> categoryOptional =
                accountExportService.getAccountById(UUID.fromString(guid));
        if (categoryOptional.isPresent()) {
            return ResponseEntity.ok().body(categoryOptional.get());
        } else {
            throw new ErrorCodeException(UNPROCESSABLE_ENTITY, Constants.ACCOUNT_NOT_FOUND + guid);
        }
    }


    private ResponseEntity<PagedResources> generateResponse(Page<AccountProjection> projections) {
        if (projections.hasContent()) {
            return new ResponseEntity(accountPagedResourcesAssembler.
                    toResource(projections),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity(accountPagedResourcesAssembler.
                    toEmptyResource(projections, UIAccountProjectionResource.class,
                            null), HttpStatus.OK);
        }
    }

    private String getUser() {
        return "DEVELOPER";
    }
}
