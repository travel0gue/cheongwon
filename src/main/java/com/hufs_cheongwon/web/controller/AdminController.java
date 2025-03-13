package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.common.security.CustomAdminDetails;
import com.hufs_cheongwon.common.security.CustomUserDetails;
import com.hufs_cheongwon.common.security.JwtUtil;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.service.TokenService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    //해싱한 비밀번호 값을 받아서 디비에 저장하기 위한 매세드 (나중에 다른 방법으로 바꾸는 게 좋을 것 같습니다)
    @GetMapping("/pwd")
    public ApiResponse<Void> registerUser(){
        String encodedPassword = bCryptPasswordEncoder.encode("user1234");
        System.out.println(encodedPassword);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }

    @PostMapping("/logout")
    public ApiResponse<Admin> logoutUser(HttpServletRequest request, @AuthenticationPrincipal CustomAdminDetails customAdminDetails) {

        //헤더에서 access token 추출
        String accessToken = jwtUtil.resolveAccessToken(request);
        String username = jwtUtil.getUsername(accessToken);

        //access token 블랙리스트 등록 & refresh token 삭제
        tokenService.destroyToken(username, accessToken);
        return ApiResponse.onSuccess(SuccessStatus.ADMIN_LOGOUT_SUCCESS, customAdminDetails.getAdmin());
    }
}
