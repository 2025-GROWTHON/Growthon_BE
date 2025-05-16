package com.growthon.domain.user.exception;

import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.exception.BusinessException;

public  class DuplicateUserException extends BusinessException {

    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }

}
