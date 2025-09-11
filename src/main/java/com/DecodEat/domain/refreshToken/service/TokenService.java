package com.DecodEat.domain.refreshToken.service;


import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.*;

import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.service.UserService;
import com.DecodEat.global.config.jwt.JwtTokenProvider;
import com.DecodEat.global.config.oauth.OAuth2SuccessHandler;
import com.DecodEat.global.exception.GeneralException;
import com.DecodEat.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken){
        if(!jwtTokenProvider.validToken(refreshToken)){
            throw new GeneralException(UNEXPECTED_TOKEN);
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return jwtTokenProvider.generateToken(user, Duration.ofHours(2)); // 액세스 토큰 유효시간 : 2시간
    }

    public String refreshAccessToken(HttpServletRequest request){
        Cookie cookie = CookieUtil
                .getCookie(request, OAuth2SuccessHandler.REFRESH_TOKEN_COOKIE_NAME)
                .orElseThrow(() -> new GeneralException(NO_RESULT));

        String refreshToken = cookie.getValue();

        if(!jwtTokenProvider.validToken(refreshToken)){
            throw new GeneralException(UNEXPECTED_TOKEN);
        }
        return createNewAccessToken(refreshToken);
    }
}
