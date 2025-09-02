package com.DecodEat.domain.RefreshToken.service;

import com.DecodEat.domain.RefreshToken.entity.RefreshToken;
import com.DecodEat.domain.RefreshToken.repository.RefreshTokenRepository;
import com.DecodEat.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.DecodEat.global.apiPayload.code.status.ErrorStatus.*;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GeneralException(UNEXPECTED_TOKEN));
    }

}
