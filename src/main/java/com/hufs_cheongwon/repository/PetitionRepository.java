package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetitionRepository extends JpaRepository<Petition, Long> {

    @Override
    Page<Petition> findAll(Pageable pageable);

    @Override
    Optional<Petition> findById(Long id);

    Page<Petition> findByPetitionStatus(PetitionStatus status, Pageable pageable);

    /**
     * 진행중인 청원 가져오기
     */
    default Page<Petition> findAllOngoingPetitions(Pageable pageable) {
        return findByPetitionStatus(PetitionStatus.ONGOING, pageable);
    }

    /**
     * 만료된 청원 가져오기 (EXPIRED 상태)
     */
    default Page<Petition> findAllExpiredPetitions(Pageable pageable) {
        return findByPetitionStatus(PetitionStatus.EXPIRED, pageable);
    }

    /**
     * 대기중인 청원 가져오기 (WAITING 상태)
     */
    default Page<Petition> findAllWaitingPetitions(Pageable pageable) {
        return findByPetitionStatus(PetitionStatus.WAITING, pageable);
    }

    /**
     * 답변된 청원 가져오기 (ANSWER_COMPLETED 상태)
     */
    default Page<Petition> findAllAnsweredPetitions(Pageable pageable) {
        return findByPetitionStatus(PetitionStatus.ANSWER_COMPLETED, pageable);
    }

    // 제목 또는 내용에 키워드가 포함된 청원 검색
    @Query("SELECT p FROM Petition p WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND p.petitionStatus = :petitionStatus")
    List<Petition> searchPetitionsByKeywordAndStatus(String keyword, PetitionStatus petitionStatus);

    // 최근 생성된 순으로 청원 조회
    Page<Petition> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 동의 수가 많은 순으로 청원 조회
    Page<Petition> findAllByOrderByAgreeCountDesc(Pageable pageable);
}
