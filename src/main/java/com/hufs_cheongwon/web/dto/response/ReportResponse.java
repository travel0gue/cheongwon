package com.hufs_cheongwon.web.dto.response;

import com.hufs_cheongwon.domain.Agreement;
import com.hufs_cheongwon.domain.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "청원 신고 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReportResponse {

    private Long reportId;
    private Long petitionId;
    private Long reportUserId;
    private String reportUserEmail;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .reportId(report.getId())
                .petitionId(report.getPetition().getId())
                .reportUserId(report.getUsers().getId())
                .reportUserEmail(report.getUsers().getEmail())
                .build();
    }
}