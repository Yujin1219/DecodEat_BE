package com.DecodEat.domain.products.entity;

import com.DecodEat.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_nutrition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductNutrition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

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