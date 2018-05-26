package com.silvertech.expenseTracker.service;

import com.silvertech.expenseTracker.domain.entity.Gender;
import com.silvertech.expenseTracker.domain.request.gender.GenderCreateRequest;
import com.silvertech.expenseTracker.domain.request.gender.GenderCreateRequestList;
import com.silvertech.expenseTracker.domain.resource.GenderCreateResponseBuilder;
import com.silvertech.expenseTracker.domain.response.gender.GenderCreateResponse;
import com.silvertech.expenseTracker.domain.response.gender.GenderResponse;
import com.silvertech.expenseTracker.domain.response.gender.GenderResponseList;
import com.silvertech.expenseTracker.repository.GenderRepository;
import com.silvertech.expenseTracker.utils.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GenderService {
    @Autowired
    private GenderRepository genderRepository;
    private List<GenderResponse> genderResponse;
    @Autowired
    private GenderCreateResponseBuilder genderCreateResponseBuilder;

    public Page<Gender> findAll(Pageable pageable) {
        return genderRepository.findAll(pageable);
    }

    public Gender getById(UUID id) {
        return genderRepository.getOne(id);
    }

    @Transactional
    public GenderCreateResponse createGender(List<GenderCreateRequestList> genders, String user) {
        List<GenderResponseList> genderResponseLists = new ArrayList<>();
        log.info("Input createGender {} ", genders);
        for (GenderCreateRequestList genderCreateRequestList : genders) {
            GenderResponseList genderResponseList = new GenderResponseList();
            genderResponse = new ArrayList<GenderResponse>();
            log.info("Create Request for Gender " + genderCreateRequestList);
            createNewGender(genderCreateRequestList, user);
            genderResponseList.setGenderResponses(genderResponse);
            genderResponseLists.add(genderResponseList);
        }
        log.info("Output createGender HTTP Response {} ", HttpStatus.CREATED.value());
        return new GenderCreateResponse(HttpStatus.CREATED.value(), genderResponseLists);
    }

    private void createNewGender(GenderCreateRequestList genderCreateRequestList, String user) {
        List<Gender> genderList = new ArrayList<>();
        List<Gender> responseGenderList;
        for (GenderCreateRequest genderCreateRequest : genderCreateRequestList.getGenderCreateRequests()) {
            Gender gender = buildCategory(genderCreateRequest, user);
            genderList.add(gender);
        }
        responseGenderList = genderRepository.saveAll(genderList);
        buildCategoryResponse(responseGenderList, genderResponse);
    }

    private void buildCategoryResponse(List<Gender> responseGenderList, List<GenderResponse> genderResponse) {
        responseGenderList.forEach(rgl -> genderResponse.add(genderCreateResponseBuilder.createGenderResponse(rgl)));
    }

    private Gender buildCategory(GenderCreateRequest genderCreateRequest, String user) {
        log.info("Constructing Gender " + genderCreateRequest);
        Gender gender = new Gender();
        gender.setSex(genderCreateRequest.getSex());
        gender.setLastModifiedUser(user);
        gender.setCreatedDttm(new DateFormatUtil().getCurrentPstDate());
        gender.setLastModifiedDttm(new DateFormatUtil().getCurrentPstDate());
        return gender;
    }

    public Gender updateGender(Gender gender) {
        return genderRepository.save(gender);
    }

}
