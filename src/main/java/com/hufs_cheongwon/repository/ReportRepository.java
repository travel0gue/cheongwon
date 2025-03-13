package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Report;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * 사용자와 청원 ID로 신고 정보 조회
     */
    Optional<Report> findByUsersIdAndPetitionId(Long userId, Long petitionId);

    /**
     * 사용자와 청원 ID로 신고 여부 확인
     */
    boolean existsByUsersIdAndPetitionId(Long userId, Long petitionId);

    /**
     * 청원 ID로 신고 수 조회
     */
    long countByPetitionId(Long petitionId);
}
