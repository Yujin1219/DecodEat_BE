package com.DecodEat.domain.products.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor // JSON -> Java 객체 변환 시 Jackson 라이브러리가 사용하기 위함
public class ProductBasedRecommendationResponseDto {

    private List<RecommendationDetailDto> recommendations;
    private int totalCount;
    private Long userId;
    private Long referenceProductId;
    private String recommendationType;
    private String dataQuality;
    private String message;

    @Getter
    @NoArgsConstructor
    public static class RecommendationDetailDto {
        private Long productId;
        private double similarityScore;
        private String recommendationReason;
    }
}