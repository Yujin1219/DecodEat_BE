package com.DecodEat.domain.products.service;

import com.DecodEat.domain.products.client.PythonAnalysisClient;
import com.DecodEat.domain.products.converter.ProductConverter;
import com.DecodEat.domain.products.dto.request.AnalysisRequestDto;
import com.DecodEat.domain.products.dto.request.ProductBasedRecommendationRequestDto;
import com.DecodEat.domain.products.dto.request.ProductRegisterRequestDto;
import com.DecodEat.domain.products.dto.response.*;
import com.DecodEat.domain.products.entity.*;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterial;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory;
import com.DecodEat.domain.products.repository.*;
import com.DecodEat.domain.users.entity.Behavior;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.entity.UserBehavior;
import com.DecodEat.domain.users.repository.UserBehaviorRepository;
import com.DecodEat.domain.users.repository.UserRepository;
import com.DecodEat.domain.users.service.UserBehaviorService;
import com.DecodEat.global.apiPayload.code.status.ErrorStatus;
import com.DecodEat.global.aws.s3.AmazonS3Manager;
import com.DecodEat.global.dto.PageResponseDto;
import com.DecodEat.global.exception.GeneralException;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductNutritionRepository productNutritionRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final PythonAnalysisClient pythonAnalysisClient;
    private final UserBehaviorService userBehaviorService;
    private static final int PAGE_SIZE = 12;
    private final UserRepository userRepository;
    private final ProductLikeRepository productLikeRepository;
    private final UserBehaviorRepository userBehaviorRepository;

    public ProductDetailDto getDetail(Long id, User user) {
        Product product = productRepository.findById(id).orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));
        if (user != null)
            userBehaviorService.saveUserBehavior(user, product, Behavior.VIEW);

        List<ProductInfoImage> images = productImageRepository.findByProduct(product);
        List<String> imageUrls = images.stream().map(ProductInfoImage::getImageUrl).toList();

        ProductNutrition productNutrition = productNutritionRepository.findByProduct(product).orElseThrow(() -> new GeneralException(PRODUCT_NUTRITION_NOT_EXISTED));

        // 좋아요 여부 확인
        boolean isLiked = false;
        if (user != null) {
            isLiked = productLikeRepository.existsByUserAndProduct(user, product);
        }
        return ProductConverter.toProductDetailDto(product, imageUrls, productNutrition, isLiked);
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

        // 파이썬 서버에 비동기로 분석 요청
        requestAnalysisAsync(savedProduct.getProductId(), productInfoImageUrls);

        userBehaviorService.saveUserBehavior(user, savedProduct, Behavior.REGISTER); // todo: 만약에 분석 실패?

        return ProductConverter.toProductRegisterDto(savedProduct, productInfoImageUrls);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO.ProductListResultDTO getProducts(Long cursorId, User user) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Slice<Product> slice = productRepository.findCompletedProductsByCursor(cursorId, pageable);

        // 기본값: 전부 false
        Set<Long> likedProductIds = Collections.emptySet();

        // user가 null이 아닐 때만 DB에서 좋아요 여부 조회
        if (user != null && !slice.isEmpty()) {
            List<Long> productIds = slice.getContent().stream()
                    .map(Product::getProductId)
                    .toList();

            likedProductIds = new HashSet<>(
                    productLikeRepository.findLikedProductIdsByUserAndProductIds(user, productIds)
            );
        }

        return ProductConverter.toProductListResultDTO(slice, likedProductIds);
    }

    // todo: 검색은 상품 엔티티와 1:1 매핑 불가능 -> userbehavior 어떻게?
    public List<ProductSearchResponseDto.SearchResultPrevDto> searchProducts(String productName) {

        Specification<Product> spec = Specification.where(ProductSpecification.isCompleted());

        if (StringUtils.hasText(productName)) {
            spec = spec.and(ProductSpecification.likeProductName(productName));
        }

        Pageable pageable = PageRequest.of(0, 10, Sort.by("productName").ascending());

        return productRepository.findAll(spec, pageable)
                .stream()
                .map(ProductConverter::toSearchResultPrevDto)
                .toList();
    }

    public PageResponseDto<ProductSearchResponseDto.ProductPrevDto> searchProducts(String productName, List<RawMaterialCategory> categories, Pageable pageable) {
        // Specification을 조합
        Specification<Product> spec = Specification.where(ProductSpecification.isCompleted());

        if (StringUtils.hasText(productName)) {
            spec = spec.and(ProductSpecification.likeProductName(productName));
        }

        if (categories != null && !categories.isEmpty()) {
            spec = spec.and(ProductSpecification.hasAllRawMaterialCategories(categories));
        }

        // Specification과 Pageable을 사용하여 데이터 조회
        Page<Product> pagedProducts = productRepository.findAll(spec, pageable);


        if (pageable.getPageNumber() >= pagedProducts.getTotalPages() && pagedProducts.getTotalPages() > 0) {
            throw new GeneralException(PAGE_OUT_OF_RANGE);
        }

        Page<ProductSearchResponseDto.ProductPrevDto> result = pagedProducts.map(ProductConverter::toProductPrevDto);

        return new PageResponseDto<>(result);
    }

    public PageResponseDto<ProductRegisterHistoryDto> getRegisterHistory(User user, Pageable pageable) {

        Long userId = user.getId();

        Page<Product> pagedProducts = productRepository.findByUserId(userId, pageable);
        Page<ProductRegisterHistoryDto> result = pagedProducts.map(ProductConverter::toProductRegisterHistoryDto);

        return new PageResponseDto<>(result);
    }

    public List<ProductSearchResponseDto.ProductPrevDto> getProductBasedRecommendation(Long productId, int limit) {

        ProductBasedRecommendationRequestDto request =
                new ProductBasedRecommendationRequestDto(productId.intValue(), limit);

        ProductBasedRecommendationResponseDto response =
                pythonAnalysisClient.getProductBasedRecommendation(request).block();

        if (response == null) {
            log.warn("No recommendation response for product ID: {}", productId);
            throw new GeneralException(NO_RECOMMENDATION_PRODUCT_BASED);
        }

        List<Product> productList = response.getRecommendations().stream()
                .filter(r -> r.getProduct_id() != null)
                .map(r -> productRepository.findById(r.getProduct_id())
                        .orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED)))
                .toList();

        return productList.stream().map(ProductConverter::toProductPrevDto).toList();
    }

    public UserBasedRecommendationResponseDto getUserBasedRecommendation(User user) {
        Long userId = user.getId();
        int randomCase = ThreadLocalRandom.current().nextInt(3);
        Behavior selectedBehavior = null;
        String message = "";
        switch (randomCase) {
            case 0:
                selectedBehavior = Behavior.LIKE; // 0번은 좋아요 기반
                message = "내가 최근 좋아요한 상품과 관련된 상품";
                break;
                case 1:
                selectedBehavior = Behavior.REGISTER; // 1번 등록 기반
                    message = "내가 최근 등록한 상품과 관련된 상품";
                break;
                case 2:
                selectedBehavior = Behavior.VIEW; // 2번 조회 기반
                    message = "내가 최근 조회한 상품과 관련된 상품";
                break;

        }

        Long standardProductId = productRepository.findRandomProductIdByUserIdAndBehavior(userId,selectedBehavior)
                .orElseThrow(()-> new GeneralException(NO_USER_BEHAVIOR_EXISTED));
        Product standardProduct = productRepository.findById(standardProductId).orElseThrow(()->new GeneralException(NO_RESULT));

        List<ProductSearchResponseDto.ProductPrevDto> products = getProductBasedRecommendation(standardProductId, 10);

        return UserBasedRecommendationResponseDto.builder()
                .standardProduct(ProductConverter.toSearchResultPrevDto(standardProduct))
                .message(message)
                .products(products)
                .build();
    }


    @Async
    public void requestAnalysisAsync(Long productId, List<String> imageUrls) {
        log.info("Starting async analysis for product ID: {}", productId);

        if (imageUrls == null || imageUrls.isEmpty()) {
            log.warn("No images to analyze for product ID: {}", productId);
            updateProductStatus(productId, DecodeStatus.FAILED, "No images provided for analysis");
            return;
        }

        try {
            AnalysisRequestDto request = AnalysisRequestDto.builder()
                    .image_urls(imageUrls)
                    .product_id(productId)
                    .build();

            pythonAnalysisClient.analyzeProduct(request)
                    .subscribe(
                            response -> processAnalysisResult(productId, response),
                            error -> {
                                log.error("Analysis failed for product ID: {}", productId, error);
                                updateProductStatus(productId, DecodeStatus.FAILED, "Analysis request failed: " + error.getMessage());
                            }
                    );
        } catch (Exception e) {
            log.error("Failed to send analysis request for product ID: {}", productId, e);
            updateProductStatus(productId, DecodeStatus.FAILED, "Failed to send analysis request");
        }
    }

    @Transactional
    public void processAnalysisResult(Long productId, AnalysisResponseDto response) {
        log.info("Processing analysis result for product ID: {} with status: {}", productId, response.getDecodeStatus());

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));

            // 상품 상태 업데이트
            product.setDecodeStatus(response.getDecodeStatus());
            productRepository.save(product);

            // 분석이 성공한 경우 영양정보 저장
            if (response.getDecodeStatus() == DecodeStatus.COMPLETED && response.getNutrition_info() != null) {
                saveNutritionInfo(productId, response);
            }

            log.info("Successfully processed analysis result for product ID: {}", productId);
        } catch (Exception e) {
            log.error("Failed to process analysis result for product ID: {}", productId, e);
            updateProductStatus(productId, DecodeStatus.FAILED, "Failed to process analysis result");
        }
    }

    @Transactional
    public void updateProductStatus(Long productId, DecodeStatus status, String message) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));

            product.setDecodeStatus(status);
            productRepository.save(product);

            log.info("Updated product ID: {} status to: {} - {}", productId, status, message);
        } catch (Exception e) {
            log.error("Failed to update product status for ID: {}", productId, e);
        }
    }

    private void saveNutritionInfo(Long productId, AnalysisResponseDto response) {
        log.info("Saving nutrition info for product ID: {}", productId);

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));

            // 영양정보 저장
            if (response.getNutrition_info() != null) {
                ProductNutrition nutrition = ProductNutrition.builder()
                        .product(product)
                        .calcium(parseDouble(response.getNutrition_info().getCalcium()))
                        .carbohydrate(parseDouble(response.getNutrition_info().getCarbohydrate()))
                        .cholesterol(parseDouble(response.getNutrition_info().getCholesterol()))
                        .dietaryFiber(parseDouble(response.getNutrition_info().getDietary_fiber()))
                        .energy(parseDouble(response.getNutrition_info().getEnergy()))
                        .fat(parseDouble(response.getNutrition_info().getFat()))
                        .protein(parseDouble(response.getNutrition_info().getProtein()))
                        .satFat(parseDouble(response.getNutrition_info().getSat_fat()))
                        .sodium(parseDouble(response.getNutrition_info().getSodium()))
                        .sugar(parseDouble(response.getNutrition_info().getSugar()))
                        .transFat(parseDouble(response.getNutrition_info().getTrans_fat()))
                        .build();

                productNutritionRepository.save(nutrition);
                log.info("Saved nutrition info for product ID: {}", productId);
            }

            // 원재료 정보 저장
            if (response.getIngredients() != null && !response.getIngredients().isEmpty()) {
                saveIngredients(product, response.getIngredients());
                log.info("Saved {} ingredients for product ID: {}", response.getIngredients().size(), productId);
            }

        } catch (Exception e) {
            log.error("Failed to save nutrition info for product ID: {}", productId, e);
            throw e;
        }
    }

    private void saveIngredients(Product product, List<String> ingredientNames) {
        // 기존 원재료 관계 삭제
        productRawMaterialRepository.deleteByProduct(product);

        for (String ingredientName : ingredientNames) {
            if (ingredientName != null && !ingredientName.trim().isEmpty()) {
                String trimmedIngredientName = ingredientName.trim();
                // 원재료가 이미 존재하는지 확인
                List<RawMaterial> rawMaterials = rawMaterialRepository.findByName(trimmedIngredientName);

                if (rawMaterials.isEmpty()) {
                    // 새로운 원재료 생성 (기본 카테고리는 OTHERS)
                    RawMaterial newRawMaterial = RawMaterial.builder()
                            .name(trimmedIngredientName)
                            .category(RawMaterialCategory.OTHERS)
                            .build();
                    rawMaterialRepository.save(newRawMaterial);

                    // 상품-원재료 관계 생성
                    ProductRawMaterial productRawMaterial = ProductRawMaterial.builder()
                            .product(product)
                            .rawMaterial(newRawMaterial)
                            .build();
                    productRawMaterialRepository.save(productRawMaterial);
                } else {
                    // 모든 조회된 원재료에 대해 상품-원재료 관계 생성
                    for (RawMaterial rawMaterial : rawMaterials) {
                        ProductRawMaterial productRawMaterial = ProductRawMaterial.builder()
                                .product(product)
                                .rawMaterial(rawMaterial)
                                .build();
                        productRawMaterialRepository.save(productRawMaterial);
                    }
                }
            }
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            // 숫자가 아닌 문자 제거 (단위 등)
            String cleanValue = value.replaceAll("[^0-9.]", "");
            return cleanValue.isEmpty() ? null : Double.parseDouble(cleanValue);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse double value: {}", value);
            return null;
        }
    }

    @Transactional
    public ProductLikeResponseDTO addOrUpdateLike(Long userId, Long productId) {

        // 1. 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(USER_NOT_EXISTED));

        // 2. 제품 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(PRODUCT_NOT_EXISTED));

        // 3. 기존 좋아요 여부 확인
        Optional<ProductLike> existingLike = productLikeRepository.findByUserAndProduct(user, product);

        boolean isLiked;

        if (existingLike.isPresent()) {
            // 이미 눌렀으면 → 좋아요 취소
            productLikeRepository.delete(existingLike.get());
            isLiked = false;
            userBehaviorService.deleteUserBehavior(user, product, Behavior.LIKE);

        } else {
            // 처음 누르면 → 좋아요 추가
            ProductLike productLike = ProductLike.builder()
                    .user(user)
                    .product(product)
                    .build();
            productLikeRepository.save(productLike);
            isLiked = true;
            userBehaviorService.saveUserBehavior(user, product, Behavior.LIKE);
        }
        return ProductConverter.toProductLikeDTO(productId, isLiked);
    }
}