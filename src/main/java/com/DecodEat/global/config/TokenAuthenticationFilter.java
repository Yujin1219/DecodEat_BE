package com.DecodEat.global.config;

import com.DecodEat.global.config.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter : 매 요청마다 필터 한 번만 실행 보장
    private final JwtTokenProvider jwtTokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 인증 토큰 가져오기
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);
        // 토큰 유효성 확인, 성공시 인증 정보 설정
        if(token!=null && jwtTokenProvider.validToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader){
        if(authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)){
            return null;
        }
        return authorizationHeader.substring(TOKEN_PREFIX.length());
    }
}
