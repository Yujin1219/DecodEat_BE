package com.DecodEat.domain.report.service;

import com.DecodEat.domain.products.entity.Product;
import com.DecodEat.domain.products.repository.ProductRepository;
import com.DecodEat.domain.report.converter.ReportConverter;
import com.DecodEat.domain.report.dto.request.ProductNutritionUpdateRequestDto;
import com.DecodEat.domain.report.dto.response.ReportResponseDto;
import com.DecodEat.domain.report.repository.ImageReportRepository;
import com.DecodEat.domain.report.repository.NutritionReportRepository;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
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

}
