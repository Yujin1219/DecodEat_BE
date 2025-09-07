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
            description = "사이트의 영양정보가 실제와 다른 경우 수정 요청")
    @PostMapping("/nutrition-info")
    public ApiResponse<ReportResponseDto> requestUpdateNutritionInfo(@CurrentUser User user,
                                                     @RequestParam Long productId,
                                                     @RequestBody ProductNutritionUpdateRequestDto requestDto) {

        return ApiResponse.onSuccess(reportService.requestUpdateNutrition(user, productId, requestDto));
    }

    @Operation(
            summary = "상품 사진 확인 요청",
            description = "부적절한 이미지인 경우 확인 요청")
    @PostMapping("/image")
    public ApiResponse<ReportResponseDto> requestCheckImage(@CurrentUser User user,
                                                     @RequestParam Long productId,
                                                     @RequestParam String imageUrl) {

        return ApiResponse.onSuccess(reportService.requestCheckImage(user, productId, imageUrl));
    }
}
