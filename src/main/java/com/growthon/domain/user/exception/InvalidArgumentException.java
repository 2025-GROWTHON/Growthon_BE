package com.growthon.domain.user.exception;

import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.exception.BusinessException;

public class InvalidArgumentException extends BusinessException {

    public InvalidArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }

}
