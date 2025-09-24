package com.DecodEat.domain.products.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBasedRecommendationResponseDto {
    private String message;
    private ProductSearchResponseDto.ProductPrevDto standardProduct;
    private List<ProductSearchResponseDto.ProductPrevDto> products;
}
