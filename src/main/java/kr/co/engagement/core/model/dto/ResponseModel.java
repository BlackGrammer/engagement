package kr.co.engagement.core.model.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseModel<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    private ResponseModel(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    private ResponseModel(HttpStatus httpStatus, String message) {
        this.code = httpStatus.value();
        this.message = message;
    }

    private ResponseModel(HttpStatus httpStatus, T data) {
        this(httpStatus);
        this.data = data;
    }

    private ResponseModel(HttpStatus httpStatus, String message, T data) {
        this(httpStatus, message);
        this.data = data;
    }

    public static <T> ResponseModel<T> ok() {
        return new ResponseModel<>(HttpStatus.OK);
    }

    public static <T> ResponseModel<T> ok(T data) {
        return new ResponseModel<>(HttpStatus.OK, data);
    }

    public static <T> ResponseModel<T> ok(String message, T data) {
        return new ResponseModel<>(HttpStatus.OK, message, data);
    }

    public static <T> ResponseModel<T> of(HttpStatus httpStatus, T data) {
        return new ResponseModel<>(httpStatus, data);
    }

    public static <T> ResponseModel<T> of(HttpStatus httpStatus, String message, T data) {
        return new ResponseModel<>(httpStatus, message, data);
    }
}
