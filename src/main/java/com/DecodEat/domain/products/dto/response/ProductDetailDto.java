package com.DecodEat.domain.products.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {
    @NotNull
    private Long productId;
    @NotNull
    private String name;
    @NotNull
    private String manufacturer;

    private String productImage;

    private List<String> imageUrl;

    private boolean isLiked;

    private Double calcium;
    private Double carbohydrate;
    private Double cholesterol;
    private Double dietaryFiber;
    private Double energy;
    private Double fat;
    private Double protein;
    private Double satFat;
    private Double sodium;
    private Double sugar;
    private Double transFat;

    // 상세 영양정보
    private List<String> animalProteins;
    private List<String>  plantProteins;
    private List<String>  complexCarbs;
    private List<String>  refinedCarbs;
    private List<String>  additives;
    private List<String>  allergens;
    private List<String>  others;

}
