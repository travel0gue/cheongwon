package com.hufs_cheongwon.common.exception;

import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
