package com.hufs_cheongwon.service;

import com.hufs_cheongwon.common.Constant;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import com.hufs_cheongwon.repository.AgreementRepository;
import com.hufs_cheongwon.repository.PetitionRepository;
import com.hufs_cheongwon.web.dto.response.PetitionStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetitionStatService {
    private final PetitionRepository petitionRepository;
    private final AgreementRepository agreementRepository;

    /**
     * 청원 통계 정보를 조회
     */
    public PetitionStatsDto getPetitionStats() {
        // 총 동의 수 (전체 동의 행 수)
        long totalAgreementCount = agreementRepository.count();

        // 일정 동의 수 이상인 청원 수
        long thresholdReachedCount = petitionRepository.countByAgreeCountGreaterThanEqual(Constant.THRESHOLDAGREEMENT);

        // 상태별 청원 수
        long ongoingCount = petitionRepository.countByPetitionStatus(PetitionStatus.ONGOING);
        long expiredCount = petitionRepository.countByPetitionStatus(PetitionStatus.EXPIRED);
        long waitingCount = petitionRepository.countByPetitionStatus(PetitionStatus.WAITING);
        long answeredCount = petitionRepository.countByPetitionStatus(PetitionStatus.ANSWER_COMPLETED);

        // 총 청원 수
        long totalPetitionCount = petitionRepository.count();

        return PetitionStatsDto.builder()
                .totalAgreementCount(totalAgreementCount)
                .thresholdAgreeCount(Constant.THRESHOLDAGREEMENT)
                .thresholdReachedCount(thresholdReachedCount)
                .ongoingCount(ongoingCount)
                .expiredCount(expiredCount)
                .waitingCount(waitingCount)
                .answeredCount(answeredCount)
                .build();
    }
}
