package com.DecodEat.domain.products.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NutritionInfoDto {
    private String calcium;
    private String carbohydrate;
    private String cholesterol;
    private String dietary_fiber;
    private String energy;
    private String fat;
    private String protein;
    private String sat_fat;
    private String sodium;
    private String sugar;
    private String trans_fat;
}