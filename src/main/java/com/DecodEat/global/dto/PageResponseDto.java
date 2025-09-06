package com.DecodEat.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDto<T> { // 제네릭 타입으로 다른 리스트에도 재사용 가능

    @Schema(description = "데이터 리스트")
    private List<T> content;

    @Schema(description = "현재 페이지 번호", example = "0")
    private int pageNumber;

    @Schema(description = "페이지 크기", example = "10")
    private int pageSize;

    @Schema(description = "전체 페이지 수", example = "15")
    private int totalPages;

    @Schema(description = "전체 요소 개수", example = "145")
    private long totalElements;

    @Schema(description = "마지막 페이지 여부", example = "false")
    private boolean last;

    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber()+1; // 0-based
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.last = page.isLast();
    }
}