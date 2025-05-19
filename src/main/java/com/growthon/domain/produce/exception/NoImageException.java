package com.growthon.domain.produce.exception;

import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.exception.BusinessException;

public class NoImageException extends BusinessException {
    public NoImageException(ErrorCode error) {
        super(error);
    }
}
