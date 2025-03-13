package com.hufs_cheongwon.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hufs_cheongwon.common.Constant;
import com.hufs_cheongwon.common.exception.AuthenticationException;
import com.hufs_cheongwon.common.exception.UserNotFoundException;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.service.TokenService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.request.LoginRequest;
import com.hufs_cheongwon.web.dto.response.LoginResponse;
import com.hufs_cheongwon.web.dto.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtUserLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws org.springframework.security.core.AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
                    LoginRequest.class);

            System.out.println("사용자 로그인 필터");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword(), null);

            //authenticationManager가 이메일, 비밀번호로 검증을 진행
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }
    }

    //로그인 성공시 실행하는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException{

        CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
        Users user = customUserDetails.getUser();

        // Access Token, Refresh Token 발급
        String accessToken = jwtUtil.createAccessToken(user.getEmail(), "ROLE_USER");
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail(), "ROLE_USER");

        // Refresh Token 저장
        tokenService.saveRefreshToken(user, refreshToken);

        // Refresh Token을 쿠키에 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(Constant.REFRESH_COOKIE_EXPIRATION) // 14일(7 * 24 * 60 * 60)
                .path("/")
                .build();

        response.setHeader("Set-Cookie", refreshTokenCookie.toString());

        TokenDto tokenDto = TokenDto.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .authId(user.getId())
                .email(user.getEmail())
                .role("USER")
                .tokenDto(tokenDto)
                .build();

        // 응답 바디 작성
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.onSuccess(SuccessStatus.USER_LOGIN_SUCCESS,loginResponse)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException failed) throws IOException, ServletException {

        AuthenticationException exception = new AuthenticationException(objectMapper);

        if (failed.getCause() instanceof UserNotFoundException) {
            exception.sendErrorResponse(response, ErrorStatus.USER_NOT_FOUND);
        } else if (failed instanceof BadCredentialsException) {
            exception.sendErrorResponse(response, ErrorStatus.PASSWORD_WRONG);
        } else {
            exception.sendErrorResponse(response, ErrorStatus._LOGIN_FAILURE);
        }
    }
}
