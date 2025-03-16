package com.hufs_cheongwon.web.dto.response;

import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.*;

@Schema(description = "답변 응답 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnswerResponse {

    private Long answerId;
    private String content;
    private WriterAdminInfo writerAdminInfo;
    private LocalDate createTime;

    @Schema(description = "관리자 정보 DTO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class WriterAdminInfo {
        private Long adminId;
        private String departure;
        private String role;
        private String email;
        private String phoneNumber;
    }

    @Builder
    public static AnswerResponse from(Response response, Admin admin) {
        return AnswerResponse.builder()
                .answerId(response.getId())
                .content(response.getContent())
                .createTime(response.getCreatedAt().toLocalDate())
                .writerAdminInfo(WriterAdminInfo.builder()
                        .adminId(admin.getId())
                        .departure(admin.getDeparture())
                        .role(admin.getRole())
                        .email(admin.getEmail())
                        .phoneNumber(admin.getPhoneNumber())
                        .build())
                .build();
    }
}
