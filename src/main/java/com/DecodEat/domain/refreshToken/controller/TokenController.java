package com.DecodEat.domain.RefreshToken.controller;

import com.DecodEat.domain.RefreshToken.dto.request.CreateAccessTokenRequest;
import com.DecodEat.domain.RefreshToken.dto.response.CreateAccessTokenResponse;
import com.DecodEat.domain.RefreshToken.service.TokenService;
import com.DecodEat.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    @Operation(summary = "액세스 토큰 재발급 API")
    public ApiResponse<CreateAccessTokenResponse> createAccessToken(@RequestBody CreateAccessTokenRequest request){

        String refreshToken = request.getRefreshToken();
        String newAccessToken = tokenService.createNewAccessToken(refreshToken);

        return ApiResponse.onSuccess(new CreateAccessTokenResponse(newAccessToken));
    }
}
