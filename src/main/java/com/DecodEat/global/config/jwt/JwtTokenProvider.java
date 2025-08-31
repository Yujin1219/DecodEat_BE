package com.DecodEat.global.config.jwt;

import com.DecodEat.domain.users.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class JwtTokenProvider implements InitializingBean {

    private final JwtProperties jwtProperties;
    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime()+expiredAt.toMillis()),user);
    }

    private String makeToken(Date expiry, User user){
        Date now = new Date();


        return Jwts.builder()
                // 1. 헤더 설정
                .header()
                .type("JWT")
                .and()

                // 2. 페이로드(클레임) 설정
                .claims()
                .issuer(jwtProperties.getIssuer())       // iss: 이슈어
                .issuedAt(now)                           // iat: 발급 시간
                .expiration(expiry)                      // exp: 만료 시간
                .subject(user.getEmail())                // sub: 유저 이메일
                .add("id", user.getId())                 // 비표준 클레임
                .add("role",user.getRole().getKey())
                .and()

                // 3. 서명 설정
                .signWith(key)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key) // 생성한 key로 서명 검증
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) { // 서명 불일치, 만료 등 모든 예외를 처리
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메소드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String userRole = claims.get("role", String.class);
        if (userRole == null) {
            // This can happen if the token was generated without a role.
            throw new IllegalArgumentException("Token is missing 'role' claim.");
        }

        // 2. Create the authority with the required "ROLE_" prefix.
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + userRole)
        );
        Long userId = claims.get("id", Long.class);

        // Principal 객체로 User 대신 userId를 직접 사용
        // 이렇게 하면 이 메소드 내에서 DB 조회가 필요x -> 성능에 유리
        // User 정보가 꼭 필요하다면 여기서 UserRepository를 통해 조회해야 하네.
        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
                String.valueOf(userId), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰에서 클레임(Payload)을 추출하는 메소드
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

}
