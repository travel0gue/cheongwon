package com.hufs_cheongwon.common.exception;

import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;

public class DuplicateResourceException extends CustomException{

    public DuplicateResourceException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
