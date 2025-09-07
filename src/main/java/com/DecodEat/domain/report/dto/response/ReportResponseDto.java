package com.DecodEat.domain.report.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReportResponseDto {

    @NotNull
    @Schema(name = "제품 id", example = "1")
    Long productId;

    @NotNull
    @Schema(name = "신고 유형", example = "영양 정보 수정")
    String reportContent;
}
