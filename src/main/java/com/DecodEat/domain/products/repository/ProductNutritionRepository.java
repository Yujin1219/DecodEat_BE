package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.ProductNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductNutritionRepository extends JpaRepository<ProductNutrition, Long> {
    ProductNutrition findByProduct_Id(Long id);
}
