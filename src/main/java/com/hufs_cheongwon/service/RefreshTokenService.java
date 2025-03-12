package com.hufs_cheongwon.service;

import com.hufs_cheongwon.common.exception.UserNotFoundException;
import com.hufs_cheongwon.common.security.JwtUtil;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.RefreshToken;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.repository.RefreshTokenRepository;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import com.hufs_cheongwon.web.dto.response.LoginResponse;
import com.hufs_cheongwon.web.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void saveRefreshToken(Users user, String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUsers(user)
                .orElse(RefreshToken.builder()
                        .users(user)
                        .admin(null)
                        .token(refreshToken)
                        .build());

        refreshTokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public void saveRefreshToken(Admin admin, String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByAdmin(admin)
                .orElse(RefreshToken.builder()
                        .users(null)
                        .admin(admin)
                        .token(refreshToken)
                        .build());

        refreshTokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public LoginResponse reissueToken(String role, String oldRefreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new UserNotFoundException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND));

        //토큰 인가 후 토큰 재발급
        if (role.equals("ROLE_USER")) {
            boolean isUserToken = refreshTokenRepository.isUserToken(refreshTokenEntity);
            if (isUserToken) {
                Users user = refreshTokenEntity.getUsers();
                String newAccessToken = jwtUtil.createAccessToken(user.getEmail(), role);
                String newRefreshToken = jwtUtil.createRefreshToken(user.getEmail(), role);

                // Refresh Token 교체
                refreshTokenEntity.updateToken(newRefreshToken);

                TokenDto tokenDto = TokenDto.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                return LoginResponse.builder()
                        .authId(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .tokenDto(tokenDto)
                        .build();
            } else {
                throw new UserNotFoundException(ErrorStatus.USER_REFRESH_TOKEN_INVALID);
            }
        } else if (role.equals("ROLE_ADMIN")) {
            boolean isAdminToken = refreshTokenRepository.isAdminToken(refreshTokenEntity);
            if (isAdminToken) {
                Admin admin = refreshTokenEntity.getAdmin();
                String newAccessToken = jwtUtil.createAccessToken(admin.getEmail(), role);
                String newRefreshToken = jwtUtil.createRefreshToken(admin.getEmail(), role);

                // Refresh Token 교체
                refreshTokenEntity.updateToken(newRefreshToken);

                TokenDto tokenDto = TokenDto.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
                return LoginResponse.builder()
                        .authId(admin.getId())
                        .email(admin.getEmail())
                        .role(admin.getRole())
                        .tokenDto(tokenDto)
                        .build();
            } else {
                throw new UserNotFoundException(ErrorStatus.ADMIN_REFRESH_TOKEN_INVALID);
            }
        } else {
            throw new UserNotFoundException(ErrorStatus.USER_REFRESH_TOKEN_INVALID);
        }
    }
}
