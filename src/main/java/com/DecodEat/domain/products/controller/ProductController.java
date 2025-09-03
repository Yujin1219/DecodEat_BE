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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //이 엔드포인트가 multipart/form-data 타입의 요청 본문을 소비(consume)한다는 것을 명확하게 선언
    public ApiResponse<ProductRegisterResponseDto> registerProduct(
            @CurrentUser User user,
            @RequestParam("name") String name,
            @RequestParam("manufacturer") String manufacturer,
            @RequestPart("productImage") MultipartFile productImage,
            @RequestPart("productInfoImages") List<MultipartFile> productInfoImages
    ) {
        ProductRegisterRequestDto requestDto = ProductRegisterRequestDto.builder()
                .name(name)
                .manufacturer(manufacturer)
                .build();

        return ApiResponse.onSuccess(productService.addProduct(user, requestDto, productImage, productInfoImages));
    }


}
