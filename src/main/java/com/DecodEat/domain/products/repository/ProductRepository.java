package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // 최신순 (ID 기준 내림차순) + decode_status = 'COMPLETED'
    @Query("SELECT p FROM Product p " +
            "WHERE p.decodeStatus = 'COMPLETED' " +
            "AND (:cursorId IS NULL OR p.productId < :cursorId) " +
            "ORDER BY p.productId DESC")
    Slice<Product> findCompletedProductsByCursor(@Param("cursorId") Long cursorId,
                                             Pageable pageable);
}
