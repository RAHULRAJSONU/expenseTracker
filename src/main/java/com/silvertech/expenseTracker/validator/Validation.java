package com.silvertech.expenseTracker.validator;

public final class Validation {

    public static final String GUID_REGULAR_EXPRESSION =
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    public static final String CATEGORY_ID_ERROR_MESSAGE =
            "Category Id must match " + GUID_REGULAR_EXPRESSION;
    public static final String INVALID_ID_ERROR_MESSAGE =
            "Id must match " + GUID_REGULAR_EXPRESSION;
    public static final String INVALID_CATEGORY_NAME_ERROR_MESSAGE =
            "Invalid Category Name";
    public static final String DESCRIPTION_REGULAR_EXPRESSION =
            "^[a-zA-Z]+$";
    public static final String BOOLEAN_REGULAR_EXPRESSION =
            "^(true|false)$";

    private Validation() {
    }

}
