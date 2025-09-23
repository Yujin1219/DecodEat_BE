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
    private int total_count;
    private Long user_id;
    private Long reference_product_id;
    private String recommendation_type;
    private String data_quality;
    private String message;

    @Getter
    @NoArgsConstructor
    public static class RecommendationDetailDto {
        private Long product_id;
        private double similarity_score;
        private String recommendation_reason;
    }
}