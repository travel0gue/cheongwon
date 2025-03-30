package com.hufs_cheongwon.common.exception;

import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;

public class BusinessException extends CustomException {
    public BusinessException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
