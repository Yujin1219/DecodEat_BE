package com.DecodEat.domain.products.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "제품 좋아요 응답 정보")
public class ProductLikeResponseDTO {

    @Schema(description = "제품 ID", example = "1")
    private Long productId;

    @Schema(description = "좋아요 여부", example = "true")
    private boolean isLiked;

}
