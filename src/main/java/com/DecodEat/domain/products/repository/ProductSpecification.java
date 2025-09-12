package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.DecodeStatus;
import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductNutrition;
import com.DecodEat.domain.products.entity.ProductRawMaterial;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterial;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

    public static Specification<Product> hasAllRawMaterialCategories(List<RawMaterialCategory> categories) {
        return (root, query, criteriaBuilder) -> {

            // 1. 서브쿼리를 생성합니다. 이 서브쿼리는 조건에 맞는 Product의 ID(Long)를 반환할 것입니다.
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Product> subRoot = subquery.from(Product.class); // 서브쿼리에서 Product 테이블을 기준으로 삼습니다.

            // 2. 서브쿼리 내에서 필요한 조인을 수행합니다.
            Join<Product, ProductRawMaterial> subProductRawMaterialJoin = subRoot.join("ingredients");
            Join<ProductRawMaterial, RawMaterial> subRawMaterialJoin = subProductRawMaterialJoin.join("rawMaterial");

            // 3. 서브쿼리의 핵심 로직: '상품별로 카테고리를 묶고(GROUP BY), 그 개수가 일치하는지 확인(HAVING)'
            subquery.select(subRoot.get("id")) // 조건에 맞는 Product의 ID를 선택
                    .where(subRawMaterialJoin.get("category").in(categories)) // 먼저 카테고리가 검색 대상에 포함되는 제품만 필터링
                    .groupBy(subRoot.get("id")) // Product ID 별로 그룹화
                    .having(criteriaBuilder.equal(
                            criteriaBuilder.countDistinct(subRawMaterialJoin.get("category")), // 각 제품의 유니크한 카테고리 개수를 세고
                            categories.size() // 그 개수가 우리가 찾는 카테고리 리스트의 전체 개수와 같은지 확인
                    ));

            // 4. 메인 쿼리: Product의 ID가 위 서브쿼리 결과 목록에 포함(IN)되는 제품만 최종 선택합니다.
            return root.get("id").in(subquery);
        };
    }
}
