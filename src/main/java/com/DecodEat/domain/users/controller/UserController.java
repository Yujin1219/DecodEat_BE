package com.DecodEat.domain.users.controller;

import com.DecodEat.domain.users.converter.UserConverter;
import com.DecodEat.domain.users.dto.response.UserInfoDto;
import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.service.UserService;
import com.DecodEat.global.apiPayload.ApiResponse;
import com.DecodEat.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/api/user")
    public ApiResponse<UserInfoDto> getMyInfo(@CurrentUser User user) {
        return ApiResponse.onSuccess(UserConverter.userToUserInfoDto(user));
    }

    @PostMapping("/api/logout")
    public ApiResponse<String> logout(@CurrentUser User user) {

        return ApiResponse.onSuccess("로그아웃.");
    }
}
