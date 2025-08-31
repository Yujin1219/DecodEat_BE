package com.DecodEat.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.*;
import java.util.Base64;
import java.util.Optional;

public class CookieUtil {
    // 요청 객체(request)에서 특정 이름의 쿠키를 찾아 반환하는 메소드
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    // 응답 객체(response)에 쿠키를 추가하는 메소드
    // httpOnly: true -> 자바스크립트에서 쿠키에 접근 불가
    // secure: true -> HTTPS 통신에서만 쿠_cookie 전송
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); // 쿠키가 적용될 경로
        cookie.setMaxAge(maxAge); // 쿠키의 유효 기간(초 단위)
        cookie.setHttpOnly(true); // JavaScript를 통한 접근 방지
        // cookie.setSecure(true); // HTTPS를 사용하는 경우에만 활성화
        response.addCookie(cookie);
    }

    // 특정 이름의 쿠키를 삭제하는 메소드
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // 유효 기간을 0으로 설정하여 즉시 만료
                    response.addCookie(cookie);
                }
            }
        }
    }

    // 객체를 직렬화하여 쿠키 값으로 변환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화하여 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
        return cls.cast(SerializationUtils.deserialize(decodedBytes));
    }
}
