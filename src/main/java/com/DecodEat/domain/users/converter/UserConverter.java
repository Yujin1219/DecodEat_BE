package com.DecodEat.domain.users.converter;

import com.DecodEat.domain.users.dto.response.UserInfoDto;
import com.DecodEat.domain.users.entity.User;

public class UserConverter {
    public static UserInfoDto userToUserInfoDto(User user){
        return UserInfoDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
