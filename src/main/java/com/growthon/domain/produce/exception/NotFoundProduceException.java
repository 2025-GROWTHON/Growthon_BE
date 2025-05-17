package com.growthon.domain.produce.exception;

import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.exception.NotFoundException;

public class NotFoundProduceException extends NotFoundException {
    public NotFoundProduceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
