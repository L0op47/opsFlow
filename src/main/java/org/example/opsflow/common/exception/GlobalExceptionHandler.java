package org.example.opsflow.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.opsflow.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ApiResponse<Void> body = ApiResponse.error(
                e.getErrorCode(),
                e.getMessage()
        );
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(body);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        String message;
        if(fieldError == null){
            message = ErrorCode.INVALID_REQUEST_PARAMETER.getDefaultMessage();
        }
        else {
            message = fieldError.getDefaultMessage();
        }
        ApiResponse<Void> body = ApiResponse.error(
                ErrorCode.INVALID_REQUEST_PARAMETER,
                message
        );
        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST_PARAMETER.getHttpStatus())
                .body(body);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("未处理的系统异常", e);

        ApiResponse<Void> body = ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(body);
    }
}