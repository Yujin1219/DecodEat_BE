package com.DecodEat.domain.report.converter;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.report.dto.request.ProductNutritionUpdateRequestDto;
import com.DecodEat.domain.report.dto.response.ReportResponseDto;
import com.DecodEat.domain.report.entity.ImageReport;
import com.DecodEat.domain.report.entity.NutritionReport;
import com.DecodEat.domain.report.entity.ReportStatus;
import com.DecodEat.domain.users.entity.User;

public class ReportConverter {
    public static ReportResponseDto toReportResponseDto(Long productId, String type){
        return ReportResponseDto.builder()
                .productId(productId)
                .reportContent(type)
                .build();
    }

    public static NutritionReport toNutritionReport(Long reporterId, Product product,ProductNutritionUpdateRequestDto requestDto){
        return NutritionReport.builder()
                .product(product)
                .reporterId(reporterId)
                .reportStatus(ReportStatus.IN_PROGRESS)
                .calcium(requestDto.getCalcium())
                .carbohydrate(requestDto.getCarbohydrate())
                .cholesterol(requestDto.getCholesterol())
                .dietaryFiber(requestDto.getDietaryFiber())
                .energy(requestDto.getEnergy())
                .fat(requestDto.getFat())
                .protein(requestDto.getProtein())
                .satFat(requestDto.getSatFat())
                .sodium(requestDto.getSodium())
                .sugar(requestDto.getSugar())
                .transFat(requestDto.getTransFat())
                .build();
    }

    public static ImageReport toImageReport(Long reporterId, Product product, String imageUrl){
        return ImageReport.builder()
                .product(product)
                .reporterId(reporterId)
                .reportStatus(ReportStatus.IN_PROGRESS)
                .imageUrl(imageUrl)
                .build();
    }

}
