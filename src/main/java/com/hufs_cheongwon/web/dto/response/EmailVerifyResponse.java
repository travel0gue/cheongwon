package com.hufs_cheongwon.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Schema(description = "로그인 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailVerifyResponse {
    private Map<String, Object> univCertResponse;
    private String emailToken;
}
