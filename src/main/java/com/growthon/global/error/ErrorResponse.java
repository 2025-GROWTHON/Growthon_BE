package com.growthon.global.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final int status;
    private final List<FieldError> errors;

    private ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.errors = null; // 일반 예외는 errors가 없음
    }

    public ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.errors = errors; // 필드 검증 오류가 있을 때만 설정
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<FieldError> errors = new ArrayList<>();
            for (org.springframework.validation.FieldError error : fieldErrors) {
                String rejectedValue = error.getRejectedValue() != null
                        ? error.getRejectedValue().toString()
                        : "";
                errors.add(new FieldError(error.getField(), rejectedValue, error.getDefaultMessage()));
            }
            return errors;
        }
    }

}