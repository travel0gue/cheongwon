package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.common.security.CustomUserDetails;
import com.hufs_cheongwon.domain.Agreement;
import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.Report;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import com.hufs_cheongwon.repository.PetitionRepository;
import com.hufs_cheongwon.service.PetitionService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.request.PetitionCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petitions")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionRepository petitionRepository;
    private final PetitionService petitionService;

    /**
     * 청원 목록 조회
     */
    @GetMapping
    public ApiResponse<Page<Petition>> getPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.PETITION_RETRIEVED, petitionRepository.findAll(pageable));
    }

    /**
     * 최근 청원 목록 조회
     */
    @GetMapping("/recent")
    public ApiResponse<Page<Petition>> getRecentPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.RECENT_PETITIONS_RETRIEVED, petitionRepository.findAllByOrderByCreatedAtDesc(pageable));
    }

    /**
     * 인기 청원 목록 조회 - 동의 수 기준
     */
    @GetMapping("/popular")
    public ApiResponse<Page<Petition>> getPopularPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.POPULAR_PETITIONS_RETRIEVED, petitionRepository.findAllByOrderByAgreeCountDesc(pageable));
    }

    /**
     * 상태별 청원 목록 조회
     */
    @GetMapping("/status/{status}")
    public ApiResponse<Page<Petition>> getPetitionsByStatus(
            @PathVariable PetitionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.PETITION_RETRIEVED, petitionRepository.findByPetitionStatus(status, pageable));
    }

    /**
     * 특정 청원 상세 조회
     * 여기에 조회수 올리는 로직 추가하면 될 듯?
     */
    @GetMapping("/{id}")
    public ApiResponse<Petition> getPetitionById(@PathVariable Long id) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_RETRIEVED, petitionService.getPetitionById(id));
    }

    /**
     * 키워드로 청원 검색
     */
    @GetMapping("/search")
    public ApiResponse<List<Petition>> searchPetitions(
            @RequestParam String keyword,
            @RequestParam(required = false) PetitionStatus status) {

        PetitionStatus searchStatus = (status != null) ? status : PetitionStatus.ONGOING;
        return ApiResponse.onSuccess(SuccessStatus.PETITION_SEARCH_SUCCESS, petitionRepository.searchPetitionsByKeywordAndStatus(keyword, searchStatus));
    }

    /**
     * 진행 중인 청원 목록 조회
     */
    @GetMapping("/ongoing")
    public ApiResponse<Page<Petition>> getOngoingPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.ONGOING_PETITIONS_RETRIEVED, petitionRepository.findAllOngoingPetitions(pageable));
    }

    /**
     * 만료된 청원 목록 조회
     */
    @GetMapping("/expired")
    public ApiResponse<Page<Petition>> getExpiredPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.EXPIRED_PETITIONS_RETRIEVED, petitionRepository.findAllExpiredPetitions(pageable));
    }

    /**
     * 대기 중인 청원 목록 조회
     */
    @GetMapping("/waiting")
    public ApiResponse<Page<Petition>> getWaitingPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.WAITING_PETITIONS_RETRIEVED, petitionRepository.findAllWaitingPetitions(pageable));
    }

    /**
     * 답변 완료된 청원 목록 조회
     */
    @GetMapping("/answered")
    public ApiResponse<Page<Petition>> getAnsweredPetitions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.ANSWERED_PETITIONS_RETRIEVED, petitionRepository.findAllAnsweredPetitions(pageable));
    }

    /**
     * 청원 등록
     */
    @PostMapping("/new")
    public ApiResponse<Petition> createPetition(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody PetitionCreateRequest petitionCreateRequest
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.PETITION_CREATED, petitionService.createPetition(
                        petitionCreateRequest, customUserDetails.getUser().getId()
                ));
    }

    /**
     * 청원 동의
     */
    @PostMapping("/{petition_id}/agree")
    public ApiResponse<Agreement> agreePetition(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("petition_id") Long petitionId
    ){
        Agreement agreement = petitionService.agreePetition(petitionId, customUserDetails.getUser().getId());
        return ApiResponse.onSuccess(SuccessStatus.PETITION_AGREE_SUCCESS, agreement);
    }

    /**
     * 청원 신고
     */
    @PostMapping("/{petition_id}/report")
    public ApiResponse<Report> reportPetition(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("petition_id") Long petitionId
    ){
        Report report = petitionService.reportPetition(petitionId, customUserDetails.getUser().getId());
        return ApiResponse.onSuccess(SuccessStatus.PETITION_REPORTED, report);
    }
}
