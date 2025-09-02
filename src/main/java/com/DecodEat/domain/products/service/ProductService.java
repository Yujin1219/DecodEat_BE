package com.DecodEat.domain.products.service;

import com.DecodEat.domain.products.converter.ProductConverter;
import com.DecodEat.domain.products.dto.response.ProductDetailDto;
import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductInfoImage;
import com.DecodEat.domain.products.entity.ProductNutrition;
import com.DecodEat.domain.products.repository.ProductImageRepository;
import com.DecodEat.domain.products.repository.ProductNutritionRepository;
import com.DecodEat.domain.products.repository.ProductRepository;
import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.*;
import com.DecodEat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductNutritionRepository productNutritionRepository;

    public ProductDetailDto getDetail(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));

        List<ProductInfoImage> images = productImageRepository.findByProduct(product);
        List<String> imageUrls = images.stream().map(ProductInfoImage::getImageUrl).toList();

        ProductNutrition productNutrition = productNutritionRepository.findById(id).orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));

        return ProductConverter.toProductDetailDto(product, imageUrls, productNutrition);
    }
}
