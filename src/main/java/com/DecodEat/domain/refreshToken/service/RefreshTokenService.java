package com.DecodEat.domain.refreshToken.service;

import com.DecodEat.domain.refreshToken.entity.RefreshToken;
import com.DecodEat.domain.refreshToken.repository.RefreshTokenRepository;
import com.DecodEat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.*;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GeneralException(UNEXPECTED_TOKEN));
    }
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
