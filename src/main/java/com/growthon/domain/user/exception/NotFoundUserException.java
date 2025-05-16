package com.growthon.domain.user.exception;

import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.exception.NotFoundException;

public class NotFoundUserException extends NotFoundException {

    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }

}