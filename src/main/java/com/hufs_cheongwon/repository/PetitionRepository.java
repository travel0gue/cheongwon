package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetitionRepository extends JpaRepository<Petition, Long> {

    @Override
    Page<Petition> findAll(Pageable pageable);

    @Override
    Optional<Petition> findById(Long id);

    Page<Petition> findByPetitionStatusOrderByCreatedAtDesc(PetitionStatus status, Pageable pageable);

    Page<Petition> findByPetitionStatusOrderByAgreeCountDesc(PetitionStatus status, Pageable pageable);

    /**
     * 진행중인 청원 가져오기
     */
    default Page<Petition> findAllOngoingPetitions(Pageable pageable) {
        return findByPetitionStatusOrderByCreatedAtDesc(PetitionStatus.ONGOING, pageable);
    }

    default Page<Petition> findAllOngoingPetitionsByAgreeCount(Pageable pageable) {
        return findByPetitionStatusOrderByAgreeCountDesc(PetitionStatus.ONGOING, pageable);
    }

    /**
     * 만료된 청원 가져오기 (EXPIRED 상태)
     */
    default Page<Petition> findAllExpiredPetitions(Pageable pageable) {
        return findByPetitionStatusOrderByCreatedAtDesc(PetitionStatus.EXPIRED, pageable);
    }

    default Page<Petition> findAllExpiredPetitionsByAgreeCount(Pageable pageable) {
        return findByPetitionStatusOrderByAgreeCountDesc(PetitionStatus.EXPIRED, pageable);
    }

    /**
     * 대기중인 청원 가져오기 (WAITING 상태)
     */
    default Page<Petition> findAllWaitingPetitions(Pageable pageable) {
        return findByPetitionStatusOrderByCreatedAtDesc(PetitionStatus.WAITING, pageable);
    }

    default Page<Petition> findAllWaitingPetitionsByAgreeCount(Pageable pageable) {
        return findByPetitionStatusOrderByAgreeCountDesc(PetitionStatus.WAITING, pageable);
    }

    /**
     * 답변된 청원 가져오기 (ANSWER_COMPLETED 상태)
     */
    default Page<Petition> findAllAnsweredPetitions(Pageable pageable) {
        return findByPetitionStatusOrderByCreatedAtDesc(PetitionStatus.ANSWER_COMPLETED, pageable);
    }

    default Page<Petition> findAllAnsweredPetitionsByAgreeCount(Pageable pageable) {
        return findByPetitionStatusOrderByAgreeCountDesc(PetitionStatus.ANSWER_COMPLETED, pageable);
    }

    // 제목 또는 내용에 키워드가 포함된 청원 검색
    @Query("SELECT p FROM Petition p WHERE (p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')) AND (:status IS NULL OR p.petitionStatus = :status)")
    List<Petition> searchPetitionsByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") PetitionStatus status);

    // 최근 생성된 순으로 청원 조회
    Page<Petition> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 동의 수가 많은 순으로 청원 조회
    Page<Petition> findAllByOrderByAgreeCountDesc(Pageable pageable);

    /**
     * 일정 동의 수 이상 받은 청원 수 조회
     */
    long countByAgreeCountGreaterThanEqual(int agreeCount);

    /**
     * 특정 상태의 청원 수 조회
     */
    long countByPetitionStatus(PetitionStatus status);

    /**
     * 30일이 경과하고 ONGOING 상태인 청원 조회
     */
    @Query("SELECT p FROM Petition p WHERE p.petitionStatus = 'ONGOING' AND p.createdAt <= :date")
    List<Petition> findExpiredOngoingPetitions(@Param("date") LocalDateTime date);

    @Query("SELECT r.petition FROM Response r WHERE r.id = :responseId")
    Petition findPetitionByResponseId(@Param("responseId") Long responseId);
}
