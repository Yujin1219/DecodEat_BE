package com.DecodEat.domain.report.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductNutritionUpdateRequestDto {
    @NotNull(message = "칼슘 정보는 필수입니다.")
    @PositiveOrZero(message = "칼슘은 0 이상의 값이어야 합니다.")
    private Double calcium;

    @NotNull(message = "탄수화물 정보는 필수입니다.")
    @PositiveOrZero(message = "탄수화물은 0 이상의 값이어야 합니다.")
    private Double carbohydrate;

    @NotNull(message = "콜레스테롤 정보는 필수입니다.")
    @PositiveOrZero(message = "콜레스테롤은 0 이상의 값이어야 합니다.")
    private Double cholesterol;

    @NotNull(message = "식이섬유 정보는 필수입니다.")
    @PositiveOrZero(message = "식이섬유는 0 이상의 값이어야 합니다.")
    private Double dietaryFiber;

    @NotNull(message = "열량 정보는 필수입니다.")
    @PositiveOrZero(message = "열량은 0 이상의 값이어야 합니다.")
    private Double energy;

    @NotNull(message = "지방 정보는 필수입니다.")
    @PositiveOrZero(message = "지방은 0 이상의 값이어야 합니다.")
    private Double fat;

    @NotNull(message = "단백질 정보는 필수입니다.")
    @PositiveOrZero(message = "단백질은 0 이상의 값이어야 합니다.")
    private Double protein;

    @NotNull(message = "포화지방 정보는 필수입니다.")
    @PositiveOrZero(message = "포화지방은 0 이상의 값이어야 합니다.")
    private Double satFat;

    @NotNull(message = "나트륨 정보는 필수입니다.")
    @PositiveOrZero(message = "나트륨은 0 이상의 값이어야 합니다.")
    private Double sodium;

    @NotNull(message = "당류 정보는 필수입니다.")
    @PositiveOrZero(message = "당류는 0 이상의 값이어야 합니다.")
    private Double sugar;

    @NotNull(message = "트랜스지방 정보는 필수입니다.")
    @PositiveOrZero(message = "트랜스지방은 0 이상의 값이어야 합니다.")
    private Double transFat;
}
