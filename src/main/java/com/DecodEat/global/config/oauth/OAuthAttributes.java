package com.DecodEat.global.config.oauth;

import com.DecodEat.domain.users.entity.Role;
import com.DecodEat.domain.users.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    // 소셜 서비스 종류에 따라 응답을 파싱하는 static 메서드
    public static OAuthAttributes of(String registrationId,
                                     Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        // 다른 소셜 서비스(e.g., google)가 있다면 여기에 추가
        // return ofGoogle("sub", attributes);

        // 지원하지 않는 소셜 서비스인 경우 예외 처리 (선택사항)
        throw new OAuth2AuthenticationException("Unsupported registrationId: " + registrationId);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // 카카오 응답에서 사용자 정보는 'kakao_account'와 'properties' 객체 안에 중첩되어 있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Users 엔티티를 생성하는 메서드
    public User toEntity() {
        return User.builder()
                .nickname(name)
                .email(email)
                .role(Role.USER) // 기본 권한
                .build();
    }
}