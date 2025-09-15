package com.DecodEat.domain.report.service;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.entity.ProductNutrition;
import com.DecodEat.domain.products.repository.ProductRepository;
import com.DecodEat.domain.report.converter.ReportConverter;
import com.DecodEat.domain.report.dto.request.ImageUpdateRequestDto;
import com.DecodEat.domain.report.dto.request.ProductNutritionUpdateRequestDto;
import com.DecodEat.domain.report.dto.response.ReportResponseDto;
import com.DecodEat.domain.report.entity.*;
import com.DecodEat.domain.report.entity.ReportRecord;
import com.DecodEat.domain.report.repository.ImageReportRepository;
import com.DecodEat.domain.report.repository.NutritionReportRepository;
import com.DecodEat.domain.report.repository.ReportRecordRepository;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.global.apiPayload.code.status.ErrorStatus;
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

    // 신고 내역 전체 조회
    @Transactional(readOnly = true)
    public ReportResponseDto.ReportListResponseDTO getReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReportRecord> reportPage = reportRecordRepository.findAllWithDetails(pageable);
        return ReportConverter.toReportListResponseDTO(reportPage);
    }

    /**
     * 단일 신고 내역 상세 조회
     * @param reportId 조회할 신고의 ID
     * @return 신고 상세 정보를 담은 DTO
     */
    @Transactional(readOnly = true)
    public ReportResponseDto.ReportListItemDTO getReportDetails(Long reportId) {

        // 1. ID로 신고 내역 조회
        ReportRecord reportRecord = reportRecordRepository.findByIdWithDetails(reportId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REPORT_NOT_FOUND));

        return ReportConverter.toReportListItemDTO(reportRecord);
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

        // 2. 이미 처리된 내역인지 확인
        if(reportRecord.getReportStatus() != ReportStatus.IN_PROGRESS) {
            throw new GeneralException(ALREADY_PROCESSED_REPORT);
        }

        // 3. reportstatus 상태를 rejected로 변경
        reportRecord.setReportStatus(ReportStatus.REJECTED);

        // 3. DTO 반환
        return ReportConverter.toReportResponseDto(reportRecord.getProduct().getProductId(), "신고 요청이 거절 처리되었습니다.");
    }


    /**
     * 상품 수정 신고 요청 수락
     * @param reportId 수락할 신고의 ID
     * @return 처리 결과를 담은 DTO
     */
    public ReportResponseDto acceptReport(Long reportId, ImageUpdateRequestDto requestDto){
        // 1. ID로 신고 내역 조회
        ReportRecord reportRecord = reportRecordRepository.findById(reportId)
                .orElseThrow(() -> new GeneralException(REPORT_NOT_FOUND));

        // 2. 이미 처리된 내역인지 확인
        if(reportRecord.getReportStatus() != ReportStatus.IN_PROGRESS) {
            throw new GeneralException(ALREADY_PROCESSED_REPORT);
        }

        Product product = reportRecord.getProduct();

        // 3. 신고 유횽에 따른 로직 분기
        if (reportRecord instanceof NutritionReport) {
            ProductNutrition productNutrition = product.getProductNutrition();
            productNutrition.updateFromReport((NutritionReport) reportRecord);

        } else if (reportRecord instanceof ImageReport) {
            // 새로운 이미지가 없는 경우 이미지 삭제 -> null로 처리
            // 새로운 이미지가 있는 경우 해당 이미지로 변경
            String newImageUrl = (requestDto != null) ? requestDto.getNewImageUrl() : null;
            product.updateProductImage(newImageUrl);
        }

        // 4. reportstatus 상태를 accepted 변경
        reportRecord.setReportStatus(ReportStatus.ACCEPTED);

        // 4. DTO 반환
        return ReportConverter.toReportResponseDto(reportRecord.getProduct().getProductId(), "신고 요청이 수락 처리되었습니다.");
    }
}
