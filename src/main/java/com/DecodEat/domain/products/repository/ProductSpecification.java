package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.DecodeStatus;
import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductNutrition;
import com.DecodEat.domain.products.entity.ProductRawMaterial;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterial;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> isCompleted() {
        return (root, query, cb) -> cb.equal(root.get("decodeStatus"), DecodeStatus.COMPLETED);
    }


    // 상품 이름으로 검색
    public static Specification<Product> likeProductName(String productName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("productName"), "%" + productName + "%");
    }

    // 특정 원재료 카테고리를 포함하는 상품 검색 (핵심 로직)
    public static Specification<Product> hasRawMaterialCategories(List<RawMaterialCategory> categories) {
        return (root, query, criteriaBuilder) -> {
            // ingredients(원재료명)으로 상품 & 상품 원재료 테이블 조인
            Join<Product, ProductRawMaterial> productRawMaterialJoin = root.join("ingredients");

            // 상품 원재료 & 원재료(세부 영양소 db) 테이블 조인
            Join<ProductRawMaterial, RawMaterial> rawMaterialJoin = productRawMaterialJoin.join("rawMaterial");

            // 원재료 DB의 세부영양소(category: ex)ALLERGENS, ANIMAL_PROTEIN...) 필터링
            query.distinct(true); // 중복 처리
            return rawMaterialJoin.get("category").in(categories);
        };
    }
}
