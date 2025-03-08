package com.hufs_cheongwon.service;

import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.RefreshToken;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(Users user, String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUser(user)
                .orElse(RefreshToken.builder()
                        .user(user)
                        .admin(null)
                        .token(refreshToken)
                        .build());

        refreshTokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void saveRefreshToken(Admin admin, String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByAdmin(admin)
                .orElse(RefreshToken.builder()
                        .user(null)
                        .admin(admin)
                        .token(refreshToken)
                        .build());

        refreshTokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }
}
