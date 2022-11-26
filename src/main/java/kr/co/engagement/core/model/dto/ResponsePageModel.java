package kr.co.engagement.core.model.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ResponsePageModel<T> implements Serializable {
    private int code;
    private String message;
    private final List<T> data;
    private final PaginationModel pagination;

    private ResponsePageModel(int code, String message, Page<T> data) {
        this.code = code;
        this.message = message;
        this.data = data.getContent();
        this.pagination = PaginationModel.of(data);
    }

    private ResponsePageModel(Page<T> data) {
        this(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    private ResponsePageModel(String message, Page<T> data) {
        this(HttpStatus.OK.value(), message, data);
    }

    private ResponsePageModel(HttpStatus httpStatus, Page<T> data) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), data);
    }

    private ResponsePageModel(HttpStatus httpStatus, String message, Page<T> data) {
        this(httpStatus.value(), message, data);
    }

    public static <T> ResponsePageModel<T> ok(Page<T> data) {
        return new ResponsePageModel<>(data);
    }

    public static <T> ResponsePageModel<T> ok(String message, Page<T> data) {
        return new ResponsePageModel<>(message, data);
    }

    public static <T> ResponsePageModel<T> of(HttpStatus httpStatus, Page<T> data) {
        return new ResponsePageModel<>(httpStatus, data);
    }

    public static <T> ResponsePageModel<T> of(HttpStatus httpStatus, String message, Page<T> data) {
        return new ResponsePageModel<>(httpStatus, message, data);
    }
}
