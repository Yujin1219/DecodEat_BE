package com.DecodEat.global.config;

import com.DecodEat.domain.refreshToken.service.RefreshTokenService;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.service.UserService;
import com.DecodEat.global.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService; // 주입
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String accessToken = getAccessToken(authorizationHeader);

        if (accessToken != null && jwtTokenProvider.validToken(accessToken)) {
            // 1. 리프레시 토큰 삭제
            Long userId = jwtTokenProvider.getUserId(accessToken);
            refreshTokenService.deleteByUserId(userId);

            // 2. User의 accessToken 필드 만료
            User user = userService.findById(userId);
            userService.expireAccessToken(user);
        }
    }

    private String getAccessToken(String authorizationHeader){
        if(authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)){
            return null;
        }
        return authorizationHeader.substring(TOKEN_PREFIX.length());
    }
}
