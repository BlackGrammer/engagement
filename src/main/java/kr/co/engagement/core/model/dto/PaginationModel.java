package kr.co.engagement.core.model.dto;

import java.io.Serializable;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PaginationModel implements Serializable {
    private final long totalElements;
    private final int numberOfElements;
    private final int totalPages;
    private final int number;
    private final int size;
    private final boolean hasNext;

    private PaginationModel(long totalElements, int numberOfElements, int totalPages, int number, int size,
        Boolean hasNext) {
        this.totalElements = totalElements;
        this.numberOfElements = numberOfElements;
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
        this.hasNext = hasNext;
    }

    public static <T> PaginationModel of(Page<T> pageData) {
        return new PaginationModel(
            pageData.getTotalElements(),
            pageData.getNumberOfElements(),
            pageData.getTotalPages(),
            pageData.getNumber(),
            pageData.getSize(),
            pageData.hasNext()
        );

    }
}
