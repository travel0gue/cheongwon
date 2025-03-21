package com.hufs_cheongwon.common.scheduler;

import com.hufs_cheongwon.common.Constant;
import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import com.hufs_cheongwon.repository.PetitionRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PetitionStatusScheduler {
    private final PetitionRepository petitionRepository;

    // 매일 자정에 실행 (cron = "초 분 시 일 월 요일")
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updatePetitionStatus() {
        log.info("Petition status update job started at: {}", LocalDateTime.now());

        // EXPIRED_DATE 경과한 ONGOING 상태의 청원을 찾음
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(Constant.EXPIRED_DATE);
        List<Petition> expiredPetitions = petitionRepository.findExpiredOngoingPetitions(thirtyDaysAgo);

        log.info("Found {} petitions to update status", expiredPetitions.size());

        for (Petition petition : expiredPetitions) {
            // 동의 수에 따라 WAITING 또는 EXPIRED로 상태 변경
            if (petition.getAgreeCount() >= Constant.THRESHOLDAGREEMENT) {
                petition.changePetitionStatus(PetitionStatus.WAITING);
                log.info("Petition ID: {} status changed to WAITING", petition.getId());
            } else {
                petition.changePetitionStatus(PetitionStatus.EXPIRED);
                log.info("Petition ID: {} status changed to EXPIRED", petition.getId());
            }
        }

        log.info("Petition status update job completed at: {}", LocalDateTime.now());
    }
}
