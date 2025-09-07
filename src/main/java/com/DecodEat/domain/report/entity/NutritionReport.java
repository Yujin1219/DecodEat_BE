package com.DecodEat.domain.report.entity;

import com.DecodEat.domain.products.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "nutrition_reports")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("NUTRITION_UPDATE")
public class NutritionReport extends ReportRecord {

    @Column(name = "calcium")
    private Double calcium;

    @Column(name = "carbohydrate")
    private Double carbohydrate;

    @Column(name = "cholesterol")
    private Double cholesterol;

    @Column(name = "dietary_fiber")
    private Double dietaryFiber;

    @Column(name = "energy")
    private Double energy;

    @Column(name = "fat")
    private Double fat;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "sat_fat")
    private Double satFat;

    @Column(name = "sodium")
    private Double sodium;

    @Column(name = "sugar")
    private Double sugar;

    @Column(name = "trans_fat")
    private Double transFat;
}
