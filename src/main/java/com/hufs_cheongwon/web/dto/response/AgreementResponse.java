package com.hufs_cheongwon.web.dto.response;

import com.hufs_cheongwon.common.Util;
import com.hufs_cheongwon.domain.Agreement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "청원 동의 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AgreementResponse {

    private Long agreementId;
    private Long petitionId;
    private Long agreementUserId;
    private String agreementUserEmail;
    private LocalDateTime createdAt;

    public static AgreementResponse from(Agreement agreement) {
        return AgreementResponse.builder()
                .agreementId(agreement.getId())
                .petitionId(agreement.getPetition().getId())
                .agreementUserId(agreement.getUsers().getId())
                .agreementUserEmail(Util.maskEmail(agreement.getUsers().getEmail()))
                .createdAt(agreement.getCreatedAt())
                .build();
    }
}
