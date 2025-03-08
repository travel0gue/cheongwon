package com.hufs_cheongwon.common.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final long emailTokenValidityInMilliseconds;

    public JwtUtil(
            // secret key
            @Value("${jwt.secret}") final String secretKey,
            // access token 유효 시간
            @Value("${jwt.accessExpiration}") final long accessTokenValidityInMilliseconds,
            // refresh token 유효 시간
            @Value("${jwt.refreshExpiration}") final long refreshTokenValidityInMilliseconds,
            // email token 유효 시간
            @Value("${jwt.emailExpiration}") final long emailTokenValidityInMilliseconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
        this.emailTokenValidityInMilliseconds = emailTokenValidityInMilliseconds;
    }

    //access token 생성
    public String createAccessToken(String subject, String role) {
        return createJwt(subject, role, accessTokenValidityInMilliseconds);
    }

    // refresh token 생성
    public String createRefreshToken(String subject, String role) {
        return createJwt(subject, role, refreshTokenValidityInMilliseconds);
    }

    // email token 생성
    public String createEmailToken(String subject, String role) {
        return createJwt(subject, role, emailTokenValidityInMilliseconds);
    }

    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
}
