package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/pwd")
    public ApiResponse<Void> registerUser(){
        String encodedPassword = bCryptPasswordEncoder.encode("user1234");
        System.out.println(encodedPassword);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }
}
