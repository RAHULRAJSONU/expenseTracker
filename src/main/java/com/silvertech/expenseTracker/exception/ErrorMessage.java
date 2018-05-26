package com.silvertech.expenseTracker.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    public static final String SUCCESS = "Success";
    public static final String DATABASE_ERROR = "Error querying database";
    public static final String NO_RESULTS_FOUND = "No results found in " +
            "Database";

    public static final String ID_ERROR = "guid must match " +
            "^[0-9a-f]{8}-[0-9a-f]{4}-" +
            "[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String UNABLE_TO_SAVE = "Unable to Save Entity";
    public static final String CATEGORY_NOT_SAVED = "Category Node not Saved";
    public static final String CATEGORY_ID_NOT_FOUND = "Category Id not found";
    public static final String INVALID_CATEGORY_ID = "Category Node Id must match ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$.";
    public static final String DUPLICATE_CATEGORY = "Duplicate Category present in request";
    public static final String CATEGORY_NOT_FOUND_IN_DB = "Category node does not exists in database";
    public static final String INVALID_REQUEST_PARAMETERS = "Invalid Request Parameters";
    public static final String USER_ID_NOT_NULL = "User id must not be null";
    public static final String IS_ACTIVE_NOT_NULL = "Active must not be null";
    public static final String CATEGORY_ID_NOT_NULL = "Category node id must not be null";
    public static final String INVALID_ACTIVE = "Active is either true or false";
    private String message;
    private List<String> id;
    private String parentId;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public ErrorMessage(String message, String parentId) {
        this.message = message;
        this.parentId = parentId;
    }

    public ErrorMessage(String message, List<String> id) {
        this.message = message;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ErrorMessage) {
            ErrorMessage temp = (ErrorMessage) obj;
            if (this.parentId == null && this.message.equalsIgnoreCase(temp.message))
                return true;
            if (this.parentId.equalsIgnoreCase(temp.parentId) && this.message.equalsIgnoreCase(temp.message))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.message == null) {
            return "".hashCode();
        }
        return this.message.hashCode();
    }
}
