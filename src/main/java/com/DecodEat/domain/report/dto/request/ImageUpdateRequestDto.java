package com.DecodEat.domain.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이미지 신고 수락 시, 새 이미지로 교체할 경우 사용하는 DTO")
public class ImageUpdateRequestDto {

    @Schema(description = "새로 등록할 상품의 대표 이미지 URL",
            example = "http://example.image.jpg",
            nullable = true)
    private String newImageUrl;
}