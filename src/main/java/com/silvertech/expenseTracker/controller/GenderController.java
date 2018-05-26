package com.silvertech.expenseTracker.controller;

import com.silvertech.expenseTracker.domain.entity.GenderProjection;
import com.silvertech.expenseTracker.domain.request.gender.GenderCreateRequestList;
import com.silvertech.expenseTracker.domain.response.gender.GenderCreateResponse;
import com.silvertech.expenseTracker.service.GenderService;
import com.silvertech.expenseTracker.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RepositoryRestController
@RequestMapping("/gender")
@Api(value = "Gender Controller", description = "All Possible Operation On Gender")
@Slf4j
@Validated
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class GenderController {
    private GenderService genderService;
    private PagedResourcesAssembler<GenderProjection> genderPagedResourcesAssembler;

    @ApiOperation(value = "POST operations on Gender")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 400, message = Constants.INPUT_VALIDATION_FAILURE),
            @ApiResponse(code = 422, message = Constants.UNPROCESSABLE_DATA),
            @ApiResponse(code = 500, message = Constants.UNEXPECTED_EXCEPTION)})
    @PostMapping(produces = {Constants.HAL_JSON_TYPE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createGender(
            @ApiParam(name = "Gender Create Request Object",
                    value = "Gender Request", required = true)
            @Validated
            @RequestBody List<GenderCreateRequestList> genders) {

        log.info("GenderCreate Request {} ", genders);

        GenderCreateResponse createUpdateResponse
                = genderService.createGender(genders, getUser());

        log.info("CategoryCreate Response {} with HTTP Status {} ",
                createUpdateResponse, HttpStatus.CREATED);
        return new ResponseEntity<GenderCreateResponse>(createUpdateResponse, HttpStatus.CREATED);
    }

    private String getUser() {
        return "DEVELOPER";
    }
}
