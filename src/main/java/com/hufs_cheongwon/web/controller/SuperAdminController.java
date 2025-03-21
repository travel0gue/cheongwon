package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.service.PetitionService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.response.PetitionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/super")
public class SuperAdminController {

    private final PetitionService petitionService;

    /**
     * 청원 삭제
     */
    @DeleteMapping("/petitions/{petition_id}/delete")
    public ApiResponse<PetitionResponse> deletePetition(
            @PathVariable("petition_id") Long petitionId
    ) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_DELETED, petitionService.deletePetition(petitionId));
    }
}
