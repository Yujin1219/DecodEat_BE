package com.DecodEat.global.config;

import com.DecodEat.domain.refreshToken.repository.RefreshTokenRepository;
import com.DecodEat.domain.users.service.UserService;
import com.DecodEat.global.config.jwt.JwtTokenProvider;
import com.DecodEat.global.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.DecodEat.global.config.oauth.OAuth2SuccessHandler;
import com.DecodEat.global.config.oauth.OAuth2UserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final CorsConfigurationSource corsConfigurationSource; // CorsCongifuragtinoSource Bean 주입 위함
    private final TokenLogoutHandler tokenLogoutHandler;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

//    @Bean
//    public WebSecurityCustomizer configure() {
//        // H2 콘솔 및 정적 리소스에 대한 시큐리티 기능 비활성화
//        return (web) -> web.ignoring()
//                .requestMatchers("/img/**", "/css/**", "/js/**", "/favicon.ico", "/error");
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 세션 정책 설정: 토큰 기반 인증을 사용하므로 STATELESS로 설정
        http.sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 2. 불필요한 기능 비활성화
        http.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (토큰 방식이므로)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .httpBasic(basic -> basic.disable()) // HTTP Basic 인증 비활성화
                .formLogin(form -> form.disable()); // 폼 기반 로그인 비활성화

        // 3. 요청별 인가 규칙 설정
        http.authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll());
                .requestMatchers("/img/**", "/css/**", "/js/**", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll() //누구나 가능
                .requestMatchers("/api/token", "/api/products/latest","/api/products/search/**").permitAll() //누구나 가능
                .requestMatchers(new RegexRequestMatcher("^/api/products/\\d+$", "GET")).permitAll()
                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN") // 유저 관련 API는 USER 또는 ADMIN 권한 필요
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // 어드민 관련 API는 ADMIN 권한만 가능
                .anyRequest().authenticated()); // 나머지 요청은 인증 필요

        // 4. OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserCustomService)) // 사용자 정보 처리 시 사용할 서비스 지정
                .successHandler(oAuth2SuccessHandler())); // 로그인 성공 시 처리할 핸들러 지정

        // 5. JWT 토큰 인증 필터 추가
        // UsernamePasswordAuthenticationFilter 앞에 커스텀 필터인 TokenAuthenticationFilter를 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        // 6. 인증/인가 예외 처리
        http.exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")));
        // 7. 로그아웃
        http.logout(logout -> logout
                .logoutUrl("/api/logout")
                .addLogoutHandler(tokenLogoutHandler)
                // 👇 카카오 로그아웃 URL로 리다이렉트
                .logoutSuccessUrl("https://kauth.kakao.com/oauth/logout?client_id=" + kakaoClientId + "&logout_redirect_uri=https://decodeat.store/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
        );

        return http.build();
    }


    // 7. 기타 필요한 Bean 등록
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                new OAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, userService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
