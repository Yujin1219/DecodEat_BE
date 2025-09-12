package com.DecodEat.domain.products.dto.response;

import com.DecodEat.domain.products.entity.DecodeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResponseDto {
    private DecodeStatus decodeStatus;
    private String product_name;
    private NutritionInfoDto nutrition_info;
    private List<String> ingredients;
    private String message;
}