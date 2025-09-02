package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
