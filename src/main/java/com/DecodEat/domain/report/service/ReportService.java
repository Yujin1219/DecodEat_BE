package com.DecodEat.domain.report.service;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.repository.ProductRepository;
import com.DecodEat.domain.report.converter.ReportConverter;
import com.DecodEat.domain.report.dto.request.ProductNutritionUpdateRequestDto;
import com.DecodEat.domain.report.dto.response.ReportResponseDto;
import com.DecodEat.domain.report.entity.ReportRecord;
import com.DecodEat.domain.report.entity.ReportStatus;
import com.DecodEat.domain.report.entity.ReportRecord;
import com.DecodEat.domain.report.repository.ImageReportRepository;
import com.DecodEat.domain.report.repository.NutritionReportRepository;
import com.DecodEat.domain.report.repository.ReportRecordRepository;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final ProductRepository productRepository;
    private final NutritionReportRepository nutritionReportRepository;
    private final ImageReportRepository imageReportRepository;
    private final ReportRecordRepository reportRecordRepository;

    public ReportResponseDto requestUpdateNutrition(User user, Long productId, ProductNutritionUpdateRequestDto requestDto){

        Product productProxy = productRepository.getReferenceById(productId); //SELECT 쿼리 없이 ID만 가진 프록시 객체를 가져옴

        nutritionReportRepository.save(ReportConverter.toNutritionReport(user.getId(), productProxy, requestDto));

        return ReportConverter.toReportResponseDto(productId,"상품 정보 업데이트 요청 완료");
    }

    public ReportResponseDto requestCheckImage(User user, Long productId, String imageUrl){

        Product productProxy = productRepository.getReferenceById(productId); //SELECT 쿼리 없이 ID만 가진 프록시 객체를 가져옴

        imageReportRepository.save(ReportConverter.toImageReport(user.getId(), productProxy, imageUrl));

        return ReportConverter.toReportResponseDto(productId,"상품 사진 확인 요청 완료");
    }

    @Transactional(readOnly = true)
    public ReportResponseDto.ReportListResponseDTO getReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReportRecord> reportPage = reportRecordRepository.findAll(pageable);
        return ReportConverter.toReportListResponseDTO(reportPage);
    }

    /**
     * 상품 수정 신고 요청 거절
     * @param reportId 거절할 신고의 ID
     * @return 처리 결과를 담은 DTO
     */
    public ReportResponseDto rejectReport(Long reportId){
        // 1. ID로 신고 내역 조회
        ReportRecord reportRecord = reportRecordRepository.findById(reportId)
                .orElseThrow(() -> new GeneralException(REPORT_NOT_FOUND));

        // 2. reportstatus 상태를 rejected로 변경
        reportRecord.setReportStatus(ReportStatus.REJECTED);

        // 3. DTO 반환
        return ReportConverter.toReportResponseDto(reportRecord.getProduct().getProductId(), "신고 요청이 거절 처리되었습니다.");
    }
}
