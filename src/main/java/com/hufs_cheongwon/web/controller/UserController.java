package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.common.Constant;
import com.hufs_cheongwon.common.security.JwtUtil;
import com.hufs_cheongwon.service.UsersService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.EmailCertifyRequest;
import com.hufs_cheongwon.web.dto.EmailSendRequest;
import com.hufs_cheongwon.web.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UsersService usersService;

    @PostMapping("/register")
    public ApiResponse<Void> registerUser(@Valid @RequestBody LoginRequest request) throws IOException{
        usersService.registerUser(request);
        return ApiResponse.onSuccess(SuccessStatus.SIGN_IN_SUCCESS, null);
    }

    //이메일 인증 코드 전송
    @PostMapping("/code/send")
    public ApiResponse<Map<String, Object>> sendEmail(@Valid @RequestBody EmailSendRequest request) throws IOException {

        Map<String, Object> response = usersService.sendEmailCode(request);

        boolean isSuccess = (boolean) response.get("success");
        if(isSuccess) {
            return ApiResponse.onSuccess(SuccessStatus.EMAIL_SENT, null);
        } else {
            return ApiResponse.onFailure( String.valueOf(response.get("code")), (String) response.get("message"), null);
        }
    }

    //코드 인증
    @PostMapping("/code/certify")
    public ApiResponse<Map<String, Object>> certifyEmail(@Valid @RequestBody EmailCertifyRequest request, HttpServletResponse httpServletResponse) throws IOException {

        Map<String, Object> response = usersService.certifyEmailCode(request);

        boolean isSuccess = (boolean) response.get("success");
        if(isSuccess) {
            return ApiResponse.onSuccess(SuccessStatus.EMAIL_VERIFIED, response);
        } else {
            return ApiResponse.onFailure( String.valueOf(response.get("code")), (String) response.get("message"), null);
        }
    }
}
