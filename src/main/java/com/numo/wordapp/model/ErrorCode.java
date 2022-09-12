package com.numo.wordapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    OperationNotAuthorized(6000, "Operation not authorized"),
    DuplicatedIdFound(6001, "Duplicate Id"),
    UnrecongizedRole(6010, "Unrecogized Role"),
    UserNotFound(6002, "UserNotFound");

    private int code;
    private String description;
}
