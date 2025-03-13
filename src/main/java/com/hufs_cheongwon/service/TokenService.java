package com.hufs_cheongwon.service;

import com.hufs_cheongwon.common.exception.UserNotFoundException;
import com.hufs_cheongwon.common.security.JwtUtil;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.BlackList;
import com.hufs_cheongwon.domain.RefreshToken;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.repository.BlackListRepository;
import com.hufs_cheongwon.repository.RefreshTokenRepository;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import com.hufs_cheongwon.web.dto.LoginResponse;
import com.hufs_cheongwon.web.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final BlackListRepository blackListRepository;

    public void saveRefreshToken(Users user, String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUsers(user)
                .orElse(RefreshToken.builder()
                        .users(user)
                        .admin(null)
                        .email(user.getEmail())
                        .token(refreshToken)
                        .build());

        refreshTokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void saveRefreshToken(Admin admin, String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByAdmin(admin)
                .orElse(RefreshToken.builder()
                        .users(null)
                        .admin(admin)
                        .email(admin.getEmail())
                        .token(refreshToken)
                        .build());

        refreshTokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public LoginResponse reissueToken(String oldRefreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new UserNotFoundException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND));

        String role = refreshTokenEntity.getRole();
        String email = refreshTokenEntity.getEmail();

        // 새로운 access token & refresh token 발급
        String newAccessToken = jwtUtil.createAccessToken(email, role);
        String newRefreshToken = jwtUtil.createRefreshToken(email, role);

        // refresh token 교체 (영속성 컨텍스트에서 관리)
        refreshTokenEntity.updateToken(newRefreshToken);

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        return LoginResponse.builder()
                .authId(refreshTokenEntity.getId())
                .email(email)
                .role(role)
                .tokenDto(tokenDto)
                .build();
    }

    public void destroyToken(String email, String token) {

        //refresh 토큰 목록에서 삭제
        refreshTokenRepository.deleteByEmail(email);

        //access 토큰 블랙리스트에 등록
        long expiration = Duration.between(Instant.now(), jwtUtil.getExpiresAtAsInstant(token)).toMinutes();
        BlackList blackList = BlackList.builder()
                .accessToken(token)
                .expiration(expiration)
                .build();

        blackListRepository.save(blackList);
    }
}
