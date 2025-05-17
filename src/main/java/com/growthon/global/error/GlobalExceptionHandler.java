package com.growthon.global.error;

import com.growthon.domain.produce.exception.NotFoundProduceException;
import com.growthon.global.error.exception.NotFoundException;
import com.growthon.global.jwt.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.growthon.global.error.exception.BusinessException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("handleException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error("handleMethodArgumentNotValidException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT, exception.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        log.error("handleBindException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT, exception.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        log.error("handleBusinessException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(exception.getErrorCode().getStatus())).body(errorResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception) {
        log.error("handleInvalidTokenException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(exception.getErrorCode().getStatus())).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        log.error("handleNotFoundException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(exception.getErrorCode().getStatus())).body(errorResponse);
    }

}