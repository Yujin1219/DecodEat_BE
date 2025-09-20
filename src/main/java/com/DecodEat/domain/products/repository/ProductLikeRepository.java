package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductLike;
import com.DecodEat.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long>  {

    Optional<ProductLike> findByUserAndProduct(User user, Product product);

}
