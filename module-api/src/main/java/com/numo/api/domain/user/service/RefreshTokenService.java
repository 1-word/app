package com.numo.api.domain.user.service;

import com.numo.domain.user.RefreshToken;
import com.numo.api.domain.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveOrUpdateToken(Long userId, String refreshToken) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(t -> t.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .userId(userId)
                                        .token(refreshToken)
                                        .build()
                        ));
    }

    public void deleteToken(Long userId) {
        refreshTokenRepository.delete(
                refreshTokenRepository.findRefreshTokenByUserId(userId)
        );
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findRefreshToken(refreshToken);
    }
}
