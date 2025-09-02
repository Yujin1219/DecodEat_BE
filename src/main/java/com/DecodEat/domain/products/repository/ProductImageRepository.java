package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductInfoImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductInfoImage, Long> {
    List<ProductInfoImage> findByProduct(Product product);
}
