package com.DecodEat.domain.products.converter;

import com.DecodEat.domain.products.dto.request.ProductRegisterRequestDto;
import com.DecodEat.domain.products.dto.response.*;
import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductInfoImage;
import com.DecodEat.domain.products.entity.ProductNutrition;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory.*;

public class ProductConverter {
    public static ProductDetailDto toProductDetailDto(Product product,
                                                      List<String> productInfoImageUrls ,
                                                      ProductNutrition productNutrition,
                                                      boolean isLiked) {
        Map<RawMaterialCategory, List<String>> nutrientsMap =
                product.getIngredients().stream()
                        .collect(Collectors.groupingBy(
                                // 그룹화의 기준(Key): ProductRawMaterial -> RawMaterial -> Category
                                productRawMaterial -> productRawMaterial.getRawMaterial().getCategory(),
                                // 그룹화된 요소(Value): ProductRawMaterial -> RawMaterial -> Name
                                Collectors.mapping(
                                        productRawMaterial -> productRawMaterial.getRawMaterial().getName(),
                                        Collectors.toList()
                                )
                        ));

        return ProductDetailDto.builder()
                .productId(product.getProductId())
                .name(product.getProductName())
                .manufacturer(product.getManufacturer())
                .productImage(product.getProductImage())
                .isLiked(isLiked)
                .calcium(productNutrition.getCalcium())
                .carbohydrate(productNutrition.getCarbohydrate())
                .cholesterol(productNutrition.getCholesterol())
                .dietaryFiber(productNutrition.getDietaryFiber())
                .energy(productNutrition.getEnergy())
                .fat(productNutrition.getFat())
                .protein(productNutrition.getProtein())
                .satFat(productNutrition.getSatFat())
                .sodium(productNutrition.getSodium())
                .sugar(productNutrition.getSugar())
                .transFat(productNutrition.getTransFat())
                .imageUrl(productInfoImageUrls)
                .animalProteins(nutrientsMap.get(ANIMAL_PROTEIN))
                .plantProteins(nutrientsMap.get(PLANT_PROTEIN))
                .complexCarbs(nutrientsMap.get(COMPLEX_CARBOHYDRATE))
                .refinedCarbs(nutrientsMap.get(REFINED_CARBOHYDRATE))
                .additives(nutrientsMap.get(ADDITIVES))
                .others(nutrientsMap.get(OTHERS))
                .allergens(nutrientsMap.get(ALLERGENS))
                .build();
    }

    public static ProductRegisterResponseDto toProductRegisterDto(Product product, List<String> productInfoImageUrls){
        return ProductRegisterResponseDto.builder()
                .name(product.getProductName())
                .manufacturer(product.getManufacturer())
                .productImage(product.getProductImage())
                .productInfoImages(productInfoImageUrls)
                .build();
    }

    // 단일 Product → ProductListItemDTO 변환
    public static ProductResponseDTO.ProductListItemDTO toProductListItemDTO(Product product){
        return ProductResponseDTO.ProductListItemDTO.builder()
                .productId(product.getProductId())
                .manufacturer(product.getManufacturer())
                .productName(product.getProductName())
                .productImage(product.getProductImage())
                .decodeStatus(product.getDecodeStatus())
                .build();
    }


    public static ProductSearchResponseDto.SearchResultPrevDto toSearchResultPrevDto(Product product){
        return ProductSearchResponseDto.SearchResultPrevDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    public static ProductSearchResponseDto.ProductPrevDto toProductPrevDto(Product product){
        return ProductSearchResponseDto.ProductPrevDto.builder()
                .productId(product.getProductId())
                .manufacturer(product.getManufacturer())
                .productName(product.getProductName())
                .productImage(product.getProductImage())
                .build();
    }

    public static ProductRegisterHistoryDto toProductRegisterHistoryDto(Product product){
        return ProductRegisterHistoryDto.builder()
                .productId(product.getProductId())
                .registerDate(product.getCreatedAt())
                .productImage(product.getProductImage())
                .decodeStatus(product.getDecodeStatus())
                .build();
    }


    // Slice<Product> → ProductListResultDTO 변환
    public static ProductResponseDTO.ProductListResultDTO toProductListResultDTO(Slice<Product> slice) {
        List<ProductResponseDTO.ProductListItemDTO> productList = slice.getContent().stream()
                .map(ProductConverter::toProductListItemDTO)
                .toList();

        Long nextCursorId = (slice.hasNext() && !productList.isEmpty())
                ? productList.get(productList.size() - 1).getProductId()
                : null;

        return ProductResponseDTO.ProductListResultDTO.builder()
                .productList(productList)
                .productListSize(productList.size())
                .isFirst(slice.isFirst())
                .hasNext(slice.hasNext())
                .nextCursorId(nextCursorId)
                .build();
    }

    public static ProductLikeResponseDTO toProductLikeDTO(Long productId, boolean isLiked) {

        return ProductLikeResponseDTO.builder()
                .productId(productId)
                .isLiked(isLiked)
                .build();

    }
}
