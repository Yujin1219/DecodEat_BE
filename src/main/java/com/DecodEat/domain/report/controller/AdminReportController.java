package com.DecodEat.domain.report.controller;

import com.DecodEat.domain.report.dto.response.ReportResponseDto;
import com.DecodEat.domain.report.service.ReportService;
import com.DecodEat.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
@Tag(name = "[관리자] 신고 관리")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportController {

    private final ReportService reportService;

    @Operation(
            summary = "상품 수정 요청 조회 (관리자)",
            description = "관리자가 모든 상품 정보 수정 요청을 페이지별로 조회합니다. 영양 정보 수정과 이미지 확인 요청을 모두 포함합니다.")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 0부터 시작합니다.", example = "0"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 항목 수", example = "10")
    })
    @GetMapping
    public ApiResponse<ReportResponseDto.ReportListResponseDTO> getReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.onSuccess(reportService.getReports(page, size));
    }

    @Operation(
            summary = "상품 수정 요청 거절 (관리자)",
            description = "관리자가 상품 정보 수정 요청을 거절합니다. 해당 신고 내역의 상태를 REJECTED로 변경합니다.")
    @Parameter(name = "reportId", description = "거절할 신고의 ID", example = "1")
    @PatchMapping("/{reportId}/reject")
    public ApiResponse<ReportResponseDto> rejectReport(@PathVariable Long reportId) {
        return ApiResponse.onSuccess(reportService.rejectReport(reportId));
    }

    @Operation(
            summary = "상품 수정 요청 수락 (관리자)",
            description = """
                    관리자가 상품 정보 수정 요청을 수락합니다. 해당 신고 내역의 상태를 ACCEPTED로 변경합니다.
                    - **영양 정보 신고 수락 시:** 실제 상품의 영양 정보가 신고된 내용으로 업데이트됩니다.
                    - **부적절 이미지 신고 수락 시:** 관리자가 새로운 이미지를 넣으면 해당 이미지로 변경되고, 넣지 않으면 이미지가 삭제됩니다.""")
    @Parameters({
            @Parameter(name = "reportId", description = "수락할 신고의 ID", example = "1", required = true)
    })
    @PatchMapping(value = "/{reportId}/accept", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReportResponseDto> acceptReport(
            @PathVariable Long reportId,
            @Parameter(name = "newImageUrl", description = "이미지 신고 시에만 사용. 교체할 새 이미지 파일을 첨부하거나, 보내지 않으면 기존 이미지가 삭제됩니다.")
            @RequestPart(value = "newImageUrl", required = false) MultipartFile newImageUrl) {
        return ApiResponse.onSuccess(reportService.acceptReport(reportId, newImageUrl));
    }

    @Operation(summary = "신고 상세 조회 API", description = "관리자가 특정 신고 내역의 상세 정보를 조회합니다.")
    @GetMapping("/{reportId}")
    public ApiResponse<ReportResponseDto.ReportListItemDTO> getReportDetetails(
            @Parameter(description = "조회할 신고의 ID", example = "1") @PathVariable Long reportId) {
        return ApiResponse.onSuccess(reportService.getReportDetails(reportId));
    }
}
