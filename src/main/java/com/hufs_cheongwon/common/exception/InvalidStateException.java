package com.hufs_cheongwon.common.exception;

import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;

public class InvalidStateException extends CustomException {
    public InvalidStateException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
