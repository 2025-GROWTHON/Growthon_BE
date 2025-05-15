package com.growthon.global.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;
    private final List<FieldError> errors;

    private ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    // 정적 팩토리 메서드: 단순 에러 응답
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    // 정적 팩토리 메서드: Validation 오류 응답
    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    /**
     * 필드 유효성 에러 내부 클래스
     */
    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        private FieldError(String field, String value, String reason) {
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