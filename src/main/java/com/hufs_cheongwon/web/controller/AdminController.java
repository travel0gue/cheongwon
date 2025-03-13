package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.common.security.CustomAdminDetails;
import com.hufs_cheongwon.common.security.JwtUtil;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Response;
import com.hufs_cheongwon.service.ResponseService;
import com.hufs_cheongwon.service.TokenService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.request.LoginRequest;
import com.hufs_cheongwon.web.dto.request.ResponseCreateRequest;
import com.hufs_cheongwon.web.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final ResponseService responseService;

    //해싱한 비밀번호 값을 받아서 디비에 저장하기 위한 매세드 (나중에 다른 방법으로 바꾸는 게 좋을 것 같습니다)
    @GetMapping("/pwd")
    public ApiResponse<Void> registerUser(){
        String encodedPassword = bCryptPasswordEncoder.encode("user1234");
        System.out.println(encodedPassword);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }

    //스웨거 문서화 용 (스웨거에서 /admin/login으로 요청을 보내도 컨트롤러로 들어오지 않고 jwtAdminLoginFilter가 가로채서 로그인을 진행합니다.)
    @PostMapping("/login")
    public ApiResponse<LoginResponse> loginAdmin(LoginRequest request){
        return ApiResponse.onSuccess(SuccessStatus.ADMIN_LOGIN_SUCCESS, null);
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



    @PostMapping("/answers/{petition_id}/new")
    public ApiResponse<Response> createResponse(
            @AuthenticationPrincipal CustomAdminDetails customAdminDetails,
            @Valid @RequestBody ResponseCreateRequest responseCreateRequest,
            @PathVariable("petition_id") Long petitionId
    ) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_ANSWERED, responseService.createResponse(
                responseCreateRequest, customAdminDetails.getAdmin().getId(), petitionId));
    }

    @DeleteMapping("/answers/{answer_id}/delete")
    public ApiResponse<Response> deleteResponse(@PathVariable("answer_id") Long answerId) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_ANSWER_DELETED, responseService.deleteResponse(answerId));
    }

    @PatchMapping("/answers/{answer_id}/update")
    public ApiResponse<Response> updateResponse(
            @PathVariable("answer_id") Long answerId,
            @Valid @RequestBody ResponseCreateRequest responseCreateRequest
    ) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_STATUS_UPDATED, responseService.updateResponse(answerId, responseCreateRequest));
    }
}
