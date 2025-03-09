package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.RefreshToken;
import com.hufs_cheongwon.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUsers(Users user);

    Optional<RefreshToken> findByAdmin(Admin admin);

    Optional<RefreshToken> findByToken(String refreshToken);

    default boolean isUserToken(RefreshToken refreshToken) {
        return refreshToken.getUsers() != null && refreshToken.getAdmin() == null;
    }

    default boolean isAdminToken(RefreshToken refreshToken) {
        return refreshToken.getAdmin() != null && refreshToken.getUsers() == null;
    }
}
