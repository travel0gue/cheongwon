package com.hufs_cheongwon.common.exception;

import com.hufs_cheongwon.web.apiResponse.error.ErrorResponse;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorStatus errorStatus = e.getErrorStatus();
        log.error("[CustomException] {}: {}", errorStatus.getCode(), errorStatus.getMessage());
        return ResponseEntity.status(errorStatus.getHttpStatus())
                .body(errorStatus.getReason());
    }

    /**
     * javax.validation.Valid 또는 @Validated 바인딩 에러시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> validationErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        log.error("[MethodArgumentNotValidException] {}", validationErrors);

        ErrorResponse errorResponse = ErrorStatus._BAD_REQUEST.getReason();
        errorResponse.setValidation(validationErrors);

        return ResponseEntity.status(ErrorStatus._BAD_REQUEST.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 요청 본문 파싱 에러
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] {}", e.getMessage());
        return ResponseEntity.status(ErrorStatus._BAD_REQUEST.getHttpStatus())
                .body(ErrorStatus._BAD_REQUEST.getReason());
    }

    /**
     * @ModelAttribute 바인딩 에러
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        Map<String, String> validationErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        log.error("[BindException] {}", validationErrors);

        ErrorResponse errorResponse = ErrorStatus._BAD_REQUEST.getReason();
        errorResponse.setValidation(validationErrors);

        return ResponseEntity.status(ErrorStatus._BAD_REQUEST.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * @RequestParam 누락
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("[MissingServletRequestParameterException] {}", e.getMessage());
        return ResponseEntity.status(ErrorStatus._BAD_REQUEST.getHttpStatus())
                .body(ErrorStatus._BAD_REQUEST.getReason());
    }

    /**
     * @RequestParam 타입 미스매치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("[MethodArgumentTypeMismatchException] {}", e.getMessage());
        return ResponseEntity.status(ErrorStatus._BAD_REQUEST.getHttpStatus())
                .body(ErrorStatus._BAD_REQUEST.getReason());
    }

    /**
     * 잘못된 HTTP 메소드 호출
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[HttpRequestMethodNotSupportedException] {}", e.getMessage());
        return ResponseEntity.status(ErrorStatus._BAD_REQUEST.getHttpStatus())
                .body(ErrorStatus._BAD_REQUEST.getReason());
    }

    /**
     * 404 에러
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("[NoHandlerFoundException] {}", e.getMessage());
        return ResponseEntity.status(ErrorStatus._NOT_FOUND.getHttpStatus())
                .body(ErrorStatus._NOT_FOUND.getReason());
    }

    /**
     * @Valid 검증 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> validationErrors = new HashMap<>();
        e.getConstraintViolations().forEach(
                violation -> {
                    String fieldName = violation.getPropertyPath().toString();
                    String message = violation.getMessage();
                    validationErrors.put(fieldName, message);
                }
        );

        log.error("[ConstraintViolationException] {}", validationErrors);

        ErrorResponse errorResponse = ErrorStatus._BAD_REQUEST.getReason();
        errorResponse.setValidation(validationErrors);

        return ResponseEntity.status(ErrorStatus._BAD_REQUEST.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[Exception] {} - {}", e.getClass().getSimpleName(), e.getMessage(), e);
        return ResponseEntity.status(ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ErrorStatus._INTERNAL_SERVER_ERROR.getReason());
    }
}