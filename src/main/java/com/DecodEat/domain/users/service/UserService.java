package com.DecodEat.domain.users.service;

import com.DecodEat.domain.users.entity.User;
import com.DecodEat.domain.users.repository.UserRepository;
import com.DecodEat.global.apiPayload.code.status.ErrorStatus;
import com.DecodEat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_EXISTED));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_EXISTED));
    }

    @Transactional
    public void saveUserAccessToken(User user, String accessToken) {
        user.updateAccessToken(accessToken);
        userRepository.save(user);
    }

    @Transactional
    public void expireAccessToken(User user) {
        user.expireAccessToken();
        userRepository.save(user);
    }

}
