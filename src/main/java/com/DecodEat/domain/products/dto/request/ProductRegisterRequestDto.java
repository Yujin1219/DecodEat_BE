package com.DecodEat.domain.products.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegisterRequestDto {
    @NotBlank(message = "제품명은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "제조사명은 필수 입력 항목입니다.")
    private String manufacturer;
}
