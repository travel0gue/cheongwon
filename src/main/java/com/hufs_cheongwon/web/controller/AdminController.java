package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/test")
    public ApiResponse<Void> registerUser(){

        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }
}
