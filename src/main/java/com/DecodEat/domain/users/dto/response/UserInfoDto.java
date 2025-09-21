package com.DecodEat.domain.users.dto.response;

import com.DecodEat.domain.users.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String email;
    private String nickname;
    private Role role;
}
