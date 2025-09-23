package com.DecodEat.domain.products.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductBasedRecommendationRequestDto {
    private int product_id;
    private int limit;
}
