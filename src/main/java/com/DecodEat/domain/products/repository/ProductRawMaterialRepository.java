package com.DecodEat.domain.products.repository;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductRawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, Long> {
    List<ProductRawMaterial> findByProduct(Product product);
    void deleteByProduct(Product product);
}