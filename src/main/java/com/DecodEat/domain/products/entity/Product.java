package com.DecodEat.domain.products.entity;

import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.entity.UserBehavior;
import com.DecodEat.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_image", nullable = true, length = 2048)
    private String productImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "decode_status", nullable = false)
    private DecodeStatus decodeStatus;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // 원재료 리스트
    private List<ProductRawMaterial> ingredients = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductNutrition productNutrition;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductInfoImage> infoImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserBehavior> userBehaviors = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProductLike> productLikes = new ArrayList<>();

    /**
     * 상품의 대표 이미지를 새로운 URL로 업데이트
     */
    public void updateProductImage(String newImageUrl) {
        this.productImage = newImageUrl;
    }
}