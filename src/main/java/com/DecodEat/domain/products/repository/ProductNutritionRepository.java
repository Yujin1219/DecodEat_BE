package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ProductNutritionRepository extends JpaRepository<ProductNutrition, Long> {
    Optional<ProductNutrition> findByProduct(Product product);
}
