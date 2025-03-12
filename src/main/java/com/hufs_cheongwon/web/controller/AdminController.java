package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.common.Constant;
import com.hufs_cheongwon.service.RefreshTokenService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/pwd")
    public ApiResponse<Void> registerUser(){
        String encodedPassword = bCryptPasswordEncoder.encode("user1234");
        System.out.println(encodedPassword);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }

    //토큰 재발급
    @PostMapping("/reissue")
    public ApiResponse<LoginResponse> reissueAdminToken(
            @CookieValue(name = "refresh_token") String refreshToken, HttpServletResponse response) throws IOException {

        LoginResponse reissueResponse = refreshTokenService.reissueToken("ROLE_Admin", refreshToken);

        // Refresh Token을 쿠키에 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(Constant.REFRESH_COOKIE_EXPIRATION) // 14일(7 * 24 * 60 * 60)
                .path("/")
                .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());

        return ApiResponse.onSuccess(SuccessStatus.REISSUE_TOKEN_SUCCESS, reissueResponse);
    }
}
