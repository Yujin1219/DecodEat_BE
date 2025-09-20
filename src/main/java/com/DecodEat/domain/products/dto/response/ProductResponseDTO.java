package com.DecodEat.domain.products.dto.response;

import com.DecodEat.domain.products.entity.DecodeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProductResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "상품 리스트 (무한스크롤)")
    public static class ProductListResultDTO {

        @Schema(description = "상품 목록")
        private List<ProductListItemDTO> productList;

        @Schema(description = "현재 페이지 상품 개수", example = "10")
        private Integer productListSize;

        @Schema(description = "페이지 처음 여부", example = "true")
        private Boolean isFirst;

        @Schema(description = "다음 페이지 여부", example = "true")
        private Boolean hasNext;

        @Schema(description = "다음 커서 ID (무한스크롤용)", example = "11")
        private Long nextCursorId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 리스트 아이템")
    public static class ProductListItemDTO {

        @Schema(description = "상품 ID", example = "1")
        private Long productId;

        @Schema(description = "제조사", example = "곰곰")
        private String manufacturer;

        @Schema(description = "상품명", example = "곰곰 육개장")
        private String productName;

        @Schema(description = "상품 이미지", example = "https://example.com/image.jpg")
        private String productImage;

        @Schema(description = "뷴석 상태", example = "COMPLETED")
        private DecodeStatus decodeStatus;

        @Schema(description = "좋아요 여부", example = "true")
        private boolean isLiked;
    }

}
