package com.silvertech.expenseTracker.domain.resource;


import com.silvertech.expenseTracker.domain.entity.Gender;
import com.silvertech.expenseTracker.domain.response.gender.GenderResponse;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class GenderCreateResponseBuilder {

    public GenderResponse createGenderResponse(Gender gender) {
        GenderResponse genderResponse = GenderResponse.builder()
                ._id(gender.getId())
                .sex(gender.getSex())
                .build();
        Link selfLink = linkTo(Gender.class)
                .slash("genders")
                .slash(genderResponse.get_id())
                .slash("?projection=genderProjection")
                .withSelfRel();

        genderResponse.add(selfLink);
        return genderResponse;
    }
}
