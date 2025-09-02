package com.DecodEat.domain.products.controller;

import com.DecodEat.domain.products.dto.response.ProductDetailDto;
import com.DecodEat.domain.products.service.ProductService;
import com.DecodEat.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

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
}
