package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductLike;
import com.DecodEat.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long>  {

    Optional<ProductLike> findByUserAndProduct(User user, Product product);

    // 여러 제품에 대한 좋아요 여부 조회 ( 제품 리스트 조회시 N + 1 문제 해결)
    @Query("SELECT pl.product.productId " +
            "FROM ProductLike pl " +
            "WHERE pl.user = :user AND pl.product.productId IN :productIds")
    List<Long> findLikedProductIdsByUserAndProductIds(@Param("user") User user,
                                                      @Param("productIds") List<Long> productIds);

    boolean existsByUserAndProduct(User user, Product product);

}
