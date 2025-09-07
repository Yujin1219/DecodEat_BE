package com.DecodEat.domain.products.dto.response;

import com.DecodEat.domain.products.entity.DecodeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductRegisterHistoryDto {

        @Schema(description = "상품 ID", example = "1")
        private Long productId;

        @Schema(description = "상품 등록일", example = "2025-09-05")
        private LocalDateTime registerDate;

        @Schema(description = "상품 이미지", example = "https://example.com/image.jpg")
        private String productImage;

        @Schema(description = "뷴석 상태", example = "COMPLETED")
        private DecodeStatus decodeStatus;
}
