package com.growthon.domain.produce.exception;

import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.exception.BusinessException;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}