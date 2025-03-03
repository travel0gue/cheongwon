package com.hufs_cheongwon.common.exception;

import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
