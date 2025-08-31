package com.DecodEat.global.config.oauth;

import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 1. 어떤 소셜 로그인인지 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 2. 소셜 로그인별로 받은 데이터를 가공
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());

        // 3. DB에 사용자 정보 저장 또는 업데이트
        User user = saveOrUpdate(attributes);

        // 4. Spring Security의 세션에 올릴 사용자 정보 구성
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        // 이메일로 사용자를 찾아보고, 없으면 새로 생성하여 저장
        return userRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> userRepository.save(attributes.toEntity()));
    }
}
