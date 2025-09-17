package com.DecodEat.global.config;

import com.DecodEat.global.common.annotation.CurrentUserArgumentResolver;
import com.DecodEat.global.common.annotation.OptionalUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final OptionalUserArgumentResolver optionalUserArgumentResolver;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(currentUserArgumentResolver);
        resolvers.add(optionalUserArgumentResolver);
    }

}
