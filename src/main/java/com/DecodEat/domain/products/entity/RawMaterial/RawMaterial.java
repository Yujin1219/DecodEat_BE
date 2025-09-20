package com.DecodEat.domain.products.entity.RawMaterial;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "raw_material")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterial { // 원재료 데이터베이스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rawMaterialId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RawMaterialCategory category; // Enum 타입으로 관리하는 것이 좋음
}
