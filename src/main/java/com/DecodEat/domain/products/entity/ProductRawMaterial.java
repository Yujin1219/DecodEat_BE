package com.DecodEat.domain.products.entity;


import com.DecodEat.domain.products.entity.RawMaterial.RawMaterial;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "product_raw_material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRawMaterial { // 상품 - 원재료 매핑테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productRawMaterialId;

    // ProductRawMaterial 입장에서 Product는 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // ProductRawMaterial 입장에서 RawMaterial은 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id")
    private RawMaterial rawMaterial;

}