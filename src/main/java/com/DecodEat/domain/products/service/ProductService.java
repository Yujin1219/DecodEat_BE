package com.DecodEat.domain.products.service;

import com.DecodEat.domain.products.converter.ProductConverter;
import com.DecodEat.domain.products.dto.request.ProductRegisterRequestDto;
import com.DecodEat.domain.products.dto.response.ProductDetailDto;
import com.DecodEat.domain.products.dto.response.ProductRegisterResponseDto;
import com.DecodEat.domain.products.entity.DecodeStatus;
import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductInfoImage;
import com.DecodEat.domain.products.entity.ProductNutrition;
import com.DecodEat.domain.products.repository.ProductImageRepository;
import com.DecodEat.domain.products.repository.ProductNutritionRepository;
import com.DecodEat.domain.products.repository.ProductRepository;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.global.aws.s3.AmazonS3Manager;
import com.DecodEat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.PRODUCT_NOT_EXISTED;
import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.PRODUCT_NUTRITION_NOT_EXISTED;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductNutritionRepository productNutritionRepository;
    private final AmazonS3Manager amazonS3Manager;

    public ProductDetailDto getDetail(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));

        List<ProductInfoImage> images = productImageRepository.findByProduct(product);
        List<String> imageUrls = images.stream().map(ProductInfoImage::getImageUrl).toList();

        ProductNutrition productNutrition = productNutritionRepository.findById(id).orElseThrow(() -> new GeneralException(PRODUCT_NUTRITION_NOT_EXISTED));

        return ProductConverter.toProductDetailDto(product, imageUrls, productNutrition);
    }

    public ProductRegisterResponseDto addProduct(User user, ProductRegisterRequestDto requestDto, MultipartFile productImage, List<MultipartFile> productInfoImages) {
        String productName = requestDto.getName();
        String manufacturer = requestDto.getManufacturer();

        String productImageUrl = null;
        if (productImage != null && !productImage.isEmpty()) {
            String productImageKey = "products/" + UUID.randomUUID() + "_" + productImage.getOriginalFilename();
            productImageUrl = amazonS3Manager.uploadFile(productImageKey, productImage);
        }


        Product newProduct = Product.builder()
                .user(user)
                .productName(productName)
                .manufacturer(manufacturer)
                .productImage(productImageUrl)
                .decodeStatus(DecodeStatus.PROCESSING)
                .build();

        Product savedProduct = productRepository.save(newProduct);

        List<String> productInfoImageUrls = null;
        if (productInfoImages != null && !productInfoImages.isEmpty()) {
            List<ProductInfoImage> infoImages = productInfoImages.stream().map(image -> {
                String imageKey = "products/info/" + UUID.randomUUID() + "_" + image.getOriginalFilename();
                String imageUrl = amazonS3Manager.uploadFile(imageKey, image);
                return ProductInfoImage.builder()
                        .product(savedProduct)
                        .imageUrl(imageUrl)
                        .build();
            }).collect(Collectors.toList());
            productImageRepository.saveAll(infoImages);

            productInfoImageUrls = infoImages.stream().map(ProductInfoImage::getImageUrl).toList();
        }

        return ProductConverter.toProductRegisterDto(savedProduct,productInfoImageUrls) ;
    }
}