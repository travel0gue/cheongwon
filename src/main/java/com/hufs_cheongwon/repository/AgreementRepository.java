package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Agreement;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    /**
     * 사용자와 청원 ID로 동의 정보 조회
     */
    Optional<Agreement> findByUsersIdAndPetitionId(Long userId, Long petitionId);

    /**
     * 사용자와 청원 ID로 동의 여부 확인
     */
    boolean existsByUsersIdAndPetitionId(Long userId, Long petitionId);

    /**
     * 청원 ID로 동의 수 조회
     */
    long countByPetitionId(Long petitionId);

    /**
     * 사용자 ID로 동의한 청원 수 조회
     */
    long countByUsersId(Long userId);
}
