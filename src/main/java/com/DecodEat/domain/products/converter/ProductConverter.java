package com.DecodEat.domain.products.converter;

import com.DecodEat.domain.products.dto.response.ProductDetailDto;
import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductNutrition;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory.*;

public class ProductConverter {
    public static ProductDetailDto toProductDetailDto(Product product,
                                                      List<String> imageUrls ,
                                                      ProductNutrition productNutrition) {
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
                .imageUrl(imageUrls)
                .animalProteins(nutrientsMap.get(ANIMAL_PROTEIN))
                .plantProteins(nutrientsMap.get(PLANT_PROTEIN))
                .complexCarbs(nutrientsMap.get(COMPLEX_CARBOHYDRATE))
                .refinedCarbs(nutrientsMap.get(REFINED_CARBOHYDRATE))
                .additives(nutrientsMap.get(ADDITIVES))
                .others(nutrientsMap.get(OTHERS))
                .allergens(nutrientsMap.get(ALLERGENS))
                .build();
    }
}
