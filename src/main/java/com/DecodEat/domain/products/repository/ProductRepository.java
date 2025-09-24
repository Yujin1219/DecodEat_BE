package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.DecodeStatus;
import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import com.DecodEat.domain.users.entity.Behavior;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // 최신순 (ID 기준 내림차순) + decode_status = 'COMPLETED'
    @Query("SELECT p FROM Product p " +
            "WHERE p.decodeStatus = 'COMPLETED' " +
            "AND (:cursorId IS NULL OR p.productId < :cursorId) " +
            "ORDER BY p.productId DESC")
    Slice<Product> findCompletedProductsByCursor(@Param("cursorId") Long cursorId,
                                             Pageable pageable);

    void deleteByDecodeStatusIn(List<DecodeStatus> statuses);

    Page<Product> findByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT p.product_id FROM product p JOIN user_behavior ub ON p.product_id = ub.product_id WHERE ub.user_id = :userId AND ub.behavior = :behavior ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Long> findRandomProductIdByUserIdAndBehavior(@Param("userId") Long userId, @Param("behavior") Behavior behavior);

    @Query("SELECT pl.product FROM ProductLike pl WHERE pl.user.id = :userId")
    Page<Product> findLikedProductsByUserId(@Param("userId") Long userId, Pageable pageable);
}
