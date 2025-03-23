package com.hufs_cheongwon.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "회원 정보 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthInfoResponse {

    private Long userId;
    private String email;
    private String role;
}
