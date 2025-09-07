package com.DecodEat.domain.report.controller;

import com.DecodEat.domain.products.dto.response.ProductDetailDto;
import com.DecodEat.domain.report.dto.request.ProductNutritionUpdateRequestDto;
import com.DecodEat.domain.report.dto.response.ReportResponseDto;
import com.DecodEat.domain.report.service.ReportService;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.global.apiPayload.ApiResponse;
import com.DecodEat.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
@Tag(name = "신고")
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "상품 정보 업데이트 요청",
            description = "단일 제품 상세 정보 조회")
    @PostMapping("/nutrition-info")
    public ApiResponse<ReportResponseDto> getProduct(@CurrentUser User user,
                                                     @RequestParam Long productId,
                                                     @RequestBody ProductNutritionUpdateRequestDto requestDto) {

        return ApiResponse.onSuccess(reportService.requestUpdateNutrition(user, productId, requestDto));
    }
}
