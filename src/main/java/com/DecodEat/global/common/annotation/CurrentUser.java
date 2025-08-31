package com.DecodEat.global.common.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 어노테이션 사용 위치
@Retention(RetentionPolicy.RUNTIME) // 어노테이션 생명주기
@Parameter(hidden = true) // 스웨거 설정
public @interface CurrentUser {
}
