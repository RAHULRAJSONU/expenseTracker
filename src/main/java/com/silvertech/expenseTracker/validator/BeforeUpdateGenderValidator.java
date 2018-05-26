package com.silvertech.expenseTracker.validator;

import com.silvertech.expenseTracker.domain.entity.Gender;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeUpdateGenderValidator")
public class BeforeUpdateGenderValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Gender.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Gender gender = (Gender) target;
        if (checkInputString(gender.getSex())) {
            errors.rejectValue("SEX", "Sex Can Not Be Empty!");
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}
