package com.silvertech.expenseTracker.utils;

public interface Constants {

    String RECORD_ALREADY_EXIST = "Category already exist";
    String ATLEAST_ONE_ADDRESS_SHOULD_BE_PRESENT = "Atleast one address has to be present";
    String CATEGORY_NOT_FOUND = "Category not found";
    String BAD_REQUEST_FOR_UPDATE = "Atleast one attribute has to be present for update";
    String SUCCESS = "Success";
    String INPUT_VALIDATION_FAILURE = "Input Validation Failure";
    String UNPROCESSABLE_DATA = "Unprocessable Data";
    String UNEXPECTED_EXCEPTION = "Unexpected Exception";
    String HAL_JSON_TYPE = "application/hal+json;charset=UTF-8";
    String PARENT_CATEGORY_DOES_NOT_EXISTS =
            "Parent Category does not exists";
    String BAD_REQUEST = "Bad Request";
    String BAD_REQUEST_FOR_CREATE = "CategoryRequestList is either null or empty";
    String CAN_NOT_SET_MULTIPLE_CURRENT_ADDRESS = "Can not set multiple current address";
    String CAN_NOT_SET_MULTIPLE_PRIMARY_EMAIL = "Can not set multiple current address";
    String CAN_NOT_SET_MULTIPLE_PRIMARY_MOBILE = "Can not set multiple current address";
    String ATLEAST_ONE_EMAIL_SHOULD_BE_PRESENT = "At-least one email has to be present";
    String ATLEAST_ONE_MOBILE_SHOULD_BE_PRESENT = "At-least one email has to be present";
    String CUSTOMER_NOT_FOUND = "Customer Not Found";
    String GENDER_NOT_FOUND = "Gender Not Found";
    String GENDER_MUST_BE_PRESENT = "Gender must be selected";
    String PARENT_ACCOUNT_DOES_NOT_EXISTS = "Parent account does not exist";
    String ACCOUNT_NOT_FOUND = "Account Does Not Exist";
    String TRANSACTION_TYPE_NOT_FOUND = "Transaction Type is not valid";
    String AMMOUNT_NOT_VALID = "Amount is not valid";
    String CAN_NOT_ACCEPT_NEGATIVE_AMMOUNT = "Can not Accept negative ammount";
    String DEBIT_AND_BENEFICIARY_ACCOUNT_CAN_NOT_BE_SAME = "Debit and Beneficiary account can not be same";
    String INSUFFICIENT_FUND = "Insufficient fund";
    String TRANSACTION_NOT_FOUND = "Transaction not found";
}
