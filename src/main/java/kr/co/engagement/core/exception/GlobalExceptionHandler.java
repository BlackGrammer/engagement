package kr.co.engagement.core.exception;

import org.springframework.core.convert.ConversionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import kr.co.engagement.core.model.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        BaseErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        BaseErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(value = {ConversionException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleArgumentException(Exception ex) {
        BaseErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseErrorCode errorCode = CommonErrorCode.NOT_FOUND;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {

        BaseErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        String errorMessage = CommonErrorCode.INVALID_PARAMETER.getMessage();
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (!ObjectUtils.isEmpty(fieldError) && StringUtils.hasLength(fieldError.getDefaultMessage())) {
            errorMessage = fieldError.getDefaultMessage();
        }
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorMessage);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        BaseErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        String errorMessage = CommonErrorCode.INVALID_PARAMETER.getMessage();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus(), errorMessage);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

}