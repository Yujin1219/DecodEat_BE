package com.DecodEat.domain.products.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegisterResponseDto {
    @NotNull
    private String name;
    @NotNull
    private String manufacturer;

    private String productImage;
    @NotNull
    private List<String> productInfoImages;
}
