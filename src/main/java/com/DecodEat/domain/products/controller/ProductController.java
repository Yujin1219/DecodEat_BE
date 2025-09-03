package com.DecodEat.domain.products.controller;

import com.DecodEat.domain.products.dto.response.ProductDetailDto;
import com.DecodEat.domain.products.dto.request.ProductRegisterRequestDto;
import com.DecodEat.domain.products.dto.response.ProductRegisterResponseDto;
import com.DecodEat.domain.products.service.ProductService;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.global.apiPayload.ApiResponse;
import com.DecodEat.global.common.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "상품")
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "제품 조회",
            description = "단일 제품 상세 정보 조회")
    @GetMapping("/{id}")
    public ApiResponse<ProductDetailDto> getProduct(@PathVariable Long id) {
        return ApiResponse.onSuccess(productService.getDetail(id));
    }

    @Operation(
            summary = "제품 등록",
            description = "상품 이미지, 제품명, 회사명으로 상품을 등록합니다")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//- **@RequestBody**: HTTP 요청 본문을 처리하며 주로 JSON, XML 데이터를 객체로 변환할 때 사용됩니다.
//
//- **@ModelAttribute**: 요청 파라미터와 폼 데이터를 처리하며, 주로 HTML 폼 데이터와 관련된 작업에 사용됩니다.
//
//- **@ParameterObject**: Springdoc OpenAPI와 함께 사용되어 API 문서화를 돕고, 여러 요청 파라미터를 하나의 객체로 그룹화하는데 사용됩니다.

    public ApiResponse<ProductRegisterResponseDto> addProduct(
            @CurrentUser User user,
            @ParameterObject ProductRegisterRequestDto request,
            @RequestPart("productImage") MultipartFile productImage,
            @RequestPart("productInfoImages") List<MultipartFile> productInfoImages
    ) {
        return ApiResponse.onSuccess(productService.addProduct(user, request, productImage, productInfoImages));
    }


}
