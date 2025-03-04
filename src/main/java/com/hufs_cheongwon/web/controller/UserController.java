package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.service.UsersService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UsersService usersService;

    @PostMapping("/register")
    public ApiResponse registerUser(UserJoinRequest request){
        usersService.registerUser(request);
        return ApiResponse.onSuccess(SuccessStatus.SIGN_IN_SUCCESS);
    }
}
