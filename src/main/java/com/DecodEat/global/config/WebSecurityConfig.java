//package com.DecodEat.global.config;
//
//import com.DecodEat.global.config.jwt.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // CSRF, Form Login, HTTP Basic 비활성화
//        http
//                .csrf(csrf -> csrf.disable())
//                .formLogin(formLogin -> formLogin.disable())
//                .httpBasic(httpBasic -> httpBasic.disable());
//
//        // 세션 관리 정책을 STATELESS(무상태)로 설정
//        http
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        // URL 별 권한 관리 설정
//        http
//                .authorizeHttpRequests(auth -> auth
//                        // Swagger 경로 인증 필요
//                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        // 인증 없이 허용할 경로
//                        .requestMatchers("/api/**").permitAll()
//                        // ADMIN 권한
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                );
//
//        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
//        http
//                .addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider),
//                        UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}