package com.numo.api.service.user;

import com.numo.domain.user.RefreshToken;
import com.numo.api.repository.user.RefreshTokenRepository;
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
                .map(t -> t.updateToken(refreshToken))
                .orElseGet(() -> refreshTokenRepository.save(
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
