package com.DecodEat.domain.report.dto.response;

import com.DecodEat.domain.report.dto.request.ProductNutritionUpdateRequestDto;
import com.DecodEat.domain.report.entity.ReportStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReportResponseDto {

    @NotNull
    @Schema(name = "제품 id", example = "1")
    Long productId;

    @NotNull
    @Schema(name = "신고 유형", example = "영양 정보 수정")
    String reportContent;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "신고된 영양정보 내용")
    public static class ReportedNutritionInfo {
        private Double calcium;
        private Double carbohydrate;
        private Double cholesterol;
        private Double dietaryFiber;
        private Double energy;
        private Double fat;
        private Double protein;
        private Double satFat;
        private Double sodium;
        private Double sugar;
        private Double transFat;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자용 신고 리스트 아이템")
    public static class ReportListItemDTO {

        @Schema(description = "신고 ID", example = "1")
        private Long reportId;

        @Schema(description = "신고자 ID", example = "2")
        private Long reporterId;

        @Schema(description = "상품 ID", example = "13")
        private Long productId;

        @Schema(description = "상품명", example = "맛있는 사과")
        private String productName;

        @Schema(description = "신고 유형", example = "NUTRITION_UPDATE")
        private String reportType;

        @Schema(description = "처리 상태", example = "IN_PROGRESS")
        private ReportStatus reportStatus;

        @Schema(description = "신고일")
        private LocalDateTime createdAt;

        @Schema(description = "신고된 이미지 URL (이미지 신고인 경우)", example = "http://example.com/inappropriate.jpg", nullable = true)
        private String imageUrl;

        @Schema(description = "신고된 영양정보 수정 요청 내용 (영양정보 신고인 경우)", nullable = true)
        private ReportedNutritionInfo nutritionRequestInfo;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자용 신고 리스트")
    public static class ReportListResponseDTO {

        @Schema(description = "신고 내역 목록")
        private List<ReportListItemDTO> reportList;

        @Schema(description = "총 페이지 수")
        private Integer totalPage;

        @Schema(description = "총 신고 내역 수")
        private Long totalElements;

        @Schema(description = "마지막 페이지 여부")
        private Boolean isLast;

        @Schema(description = "첫 페이지 여부")
        private Boolean isFirst;
    }

}
