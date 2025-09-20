package com.DecodEat.domain.products.controller;

import com.DecodEat.domain.products.dto.response.*;
import com.DecodEat.domain.products.dto.request.ProductRegisterRequestDto;
import com.DecodEat.domain.products.entity.RawMaterial.RawMaterialCategory;
import com.DecodEat.domain.products.service.ProductService;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.global.apiPayload.ApiResponse;
import com.DecodEat.global.common.annotation.CurrentUser;
import com.DecodEat.global.common.annotation.OptionalUser;
import com.DecodEat.global.dto.PageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ApiResponse<ProductDetailDto> getProduct(@PathVariable Long id,
                                                    @OptionalUser User user) {
        return ApiResponse.onSuccess(productService.getDetail(id,user));
    }

    @Operation(
            summary = "제품 등록",
            description = "상품 이미지, 제품명, 회사명으로 상품을 등록합니다")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //이 엔드포인트가 multipart/form-data 타입의 요청 본문을 소비(consume)한다는 것을 명확하게 선언
    public ApiResponse<ProductRegisterResponseDto> registerProduct(
            @CurrentUser User user,
            @RequestParam("name") String name,
            @RequestParam("manufacturer") String manufacturer,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestPart("productInfoImages") List<MultipartFile> productInfoImages
    ) {
        ProductRegisterRequestDto requestDto = ProductRegisterRequestDto.builder()
                .name(name)
                .manufacturer(manufacturer)
                .build();

        return ApiResponse.onSuccess(productService.addProduct(user, requestDto, productImage, productInfoImages));
    }

    @Operation(
            summary = "홈화면 상품 추천 (최신순)",
            description = "무한스크롤 방식으로 decode_status(분석 상태)가 COMPLETED인 상품만 최신순 정렬된 상품 목록을 조회합니다.\n" +
                    "cursorId가 없으면 첫 페이지, 있으면 해당 ID보다 작은 상품들을 불러옵니다."
    )
    @GetMapping("/latest")
    public ApiResponse<ProductResponseDTO.ProductListResultDTO> getProductList(
            @RequestParam(required = false) Long cursorId) {
        return ApiResponse.onSuccess(productService.getProducts(cursorId));
    }

    @GetMapping("/search/autocomplete")
    @Operation(summary = "상품 검색 자동완성", description = "사용자가 입력한 상품명 키워드를 기반으로 자동완성용 상품 리스트를 최대 10개까지 반환합니다.")
    public ApiResponse<List<ProductSearchResponseDto.SearchResultPrevDto>> searchProducts(
            @Parameter(description = "검색할 상품명")
            @RequestParam String productName) {

        return ApiResponse.onSuccess(productService.searchProducts(productName));
    }

    @GetMapping("/search")
    @Operation(summary = "상품 검색 및 필터링", description = "상품명과 원재료 카테고리로 상품을 검색하고 필터링합니다.")
    public ApiResponse<PageResponseDto<ProductSearchResponseDto.ProductPrevDto>> searchProducts(
            @Parameter(description = "검색할 상품명")
            @RequestParam(required = false) String productName,
            @Parameter(description = "필터링할 세부영양소 카테고리 리스트")
            @RequestParam(required = false) List<RawMaterialCategory> categories,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("productName").ascending()); // 0-based
        return ApiResponse.onSuccess(productService.searchProducts(productName, categories, pageable));
    }

    @GetMapping("/register-history")
    @Operation(summary = "나의 분석 요청 기록", description = "내가 등록한 상품의 분석 결과 목록입니다.")
    public ApiResponse<PageResponseDto<ProductRegisterHistoryDto>> getRegisterHistory(@CurrentUser User user,
                                                                                      @RequestParam(defaultValue = "1") int page,
                                                                                      @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending()); // 0-based

        return ApiResponse.onSuccess(productService.getRegisterHistory(user, pageable));
    }

    @Operation(summary = "제품 좋아요 추가/취소", description = "좋아요를 누르면 추가, 다시 누르면 취소됩니다.")
    @PostMapping("/{productId}/like")
    public ApiResponse<ProductLikeResponseDTO> addOrUpdateLike(
            @CurrentUser User user,
            @Parameter(description = "제품 ID") @PathVariable Long productId
    ) {
        return ApiResponse.onSuccess(productService.addOrUpdateLike(user.getId(), productId));
    }
}
