package com.DecodEat.domain.report.converter;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.report.dto.request.ProductNutritionUpdateRequestDto;
import com.DecodEat.domain.report.dto.response.ReportResponseDto;
import com.DecodEat.domain.report.entity.ImageReport;
import com.DecodEat.domain.report.entity.NutritionReport;
import com.DecodEat.domain.report.entity.ReportRecord;
import com.DecodEat.domain.report.entity.ReportStatus;
import com.DecodEat.domain.users.entity.User;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

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

    // 영양 정보 신고 내용 매핑
    public static ReportResponseDto.ReportedNutritionInfo toReportedNutritionInfo(NutritionReport nutritionReport){
        return ReportResponseDto.ReportedNutritionInfo.builder()
                .calcium(nutritionReport.getCalcium())
                .carbohydrate(nutritionReport.getCarbohydrate())
                .cholesterol(nutritionReport.getCholesterol())
                .dietaryFiber(nutritionReport.getDietaryFiber())
                .energy(nutritionReport.getEnergy())
                .fat(nutritionReport.getFat())
                .protein(nutritionReport.getProtein())
                .satFat(nutritionReport.getSatFat())
                .sodium(nutritionReport.getSodium())
                .sugar(nutritionReport.getSugar())
                .transFat(nutritionReport.getTransFat())
                .build();
    }

    public static ReportResponseDto.ReportListItemDTO toReportListItemDTO(ReportRecord reportRecord){
        ReportResponseDto.ReportListItemDTO.ReportListItemDTOBuilder builder = ReportResponseDto.ReportListItemDTO.builder()
                .reportId(reportRecord.getId())
                .reporterId(reportRecord.getReporterId())
                .productId(reportRecord.getProduct().getProductId())
                .productName(reportRecord.getProduct().getProductName())
                .reportStatus(reportRecord.getReportStatus())
                .createdAt(reportRecord.getCreatedAt());

        // 영양정보 관련인지 이미지 관련인지 구분
        if(reportRecord instanceof NutritionReport){
            NutritionReport nutritionReport = (NutritionReport) reportRecord;
            builder.reportType("NUTRITION_UPDATE")
                    .nutritionRequestInfo(toReportedNutritionInfo(nutritionReport));

        } else if (reportRecord instanceof ImageReport) {
            ImageReport imageReport = (ImageReport) reportRecord;
            builder.reportType("INAPPROPRIATE_IMAGE")
                    .imageUrl(imageReport.getImageUrl());
        }

        return builder.build();
    }
    public static ReportResponseDto.ReportListResponseDTO toReportListResponseDTO(Page<ReportRecord> reportPage) {
        return ReportResponseDto.ReportListResponseDTO.builder()
                // 1. 신고 목록 변환
                .reportList(reportPage.getContent().stream()
                        .map(ReportConverter::toReportListItemDTO)
                        .collect(Collectors.toList()))
                // 2. 페이징 정보 매핑
                .totalPage(reportPage.getTotalPages())
                .totalElements(reportPage.getTotalElements())
                .isFirst(reportPage.isFirst())
                .isLast(reportPage.isLast())
                .build();
    }
}
