package com.hufs_cheongwon.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetitionStatsDto {

    // 총 동의 수 (모든 청원의 동의 행 총합)
    private long totalAgreementCount;

    // 기준이 되는 동의 수
    private int thresholdAgreeCount;

    // 기준 이상의 동의를 받은 청원 수
    private long thresholdReachedCount;

    // 상태별 청원 수
    private long ongoingCount;     // 진행 중
    private long expiredCount;     // 만료
    private long waitingCount;     // 답변 중
    private long answeredCount;    // 답변 완료
}
