package com.hufs_cheongwon.common.exception;

import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
