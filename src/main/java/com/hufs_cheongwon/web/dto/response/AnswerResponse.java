package com.hufs_cheongwon.web.dto.response;

import com.hufs_cheongwon.domain.Admin;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "답변 응답 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnswerResponse {

    private Long answerId;
    private String content;
    private AdminInfo adminInfo;

    @Schema(description = "관리자 정보 DTO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class AdminInfo {
        private Long adminId;
        private String departure;
        private String role;
        private String email;
        private String phoneNumber;
    }

    @Builder
    public static AnswerResponse createResponse(Long answerId, String content, Admin admin) {
        return AnswerResponse.builder()
                .answerId(answerId)
                .content(content)
                .adminInfo(AdminInfo.builder()
                        .adminId(admin.getId())
                        .departure(admin.getDeparture())
                        .role(admin.getRole())
                        .email(admin.getEmail())
                        .phoneNumber(admin.getPhoneNumber())
                        .build())
                .build();
    }
}
