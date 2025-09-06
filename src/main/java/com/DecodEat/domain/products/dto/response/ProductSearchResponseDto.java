package com.DecodEat.domain.products.dto.response;

import com.DecodEat.domain.products.entity.DecodeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProductSearchResponseDto {
    @Getter
    @Builder
    public static class SearchResultPrevDto {

        @Schema(description = "상품 ID", example = "1")
        private Long productId;

        @Schema(description = "상품명", example = "곰곰 육개장")
        private String productName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 리스트 아이템")
    public static class ProductPrevDto {

        @Schema(description = "상품 ID", example = "1")
        private Long productId;

        @Schema(description = "제조사", example = "곰곰")
        private String manufacturer;

        @Schema(description = "상품명", example = "곰곰 육개장")
        private String productName;

        @Schema(description = "상품 이미지", example = "https://example.com/image.jpg")
        private String productImage;
    }

}
