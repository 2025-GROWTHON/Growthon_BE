package com.growthon.global.error;

import com.growthon.global.jwt.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.growthon.global.error.exception.BusinessException;

import org.springframework.validation.BindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 예상하지 못한 예외까지 포함하여 전부 잡아주는 최후의 보루 (NullPointerException, IndexOutOfBoundsException 등 개발 중 놓친 일반 런타임 예외)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("handleException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @Valid, @NotBlank, @Email, @Pattern 등 DTO 필드 검증 실패 시 발생하는 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error("handleMethodArgumentNotValidException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT, exception.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // @ModelAttribute나 폼 형식에서 타입 변환 실패, 바인딩 오류가 발생할 때 던져지는 예외 (Ex. 사용자가 "age=hello" 처럼 정수 자리에 문자를 보낼 경우)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        log.error("handleBindException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT, exception.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 도메인 로직상에서 명시적으로 발생시키는 예외 (Ex. 접근 권한이 없습니다, 해당 농산물이 존재하지 않습니다 등)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        log.error("handleBusinessException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getErrorCode().getStatus()));
    }

    // JWT 유효하지 않은 토큰 예외
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception) {
        log.error("handleInvalidTokenException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getErrorCode().getStatus()));
    }

}