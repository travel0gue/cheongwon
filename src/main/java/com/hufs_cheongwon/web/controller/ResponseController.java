package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.repository.ResponseRepository;
import com.hufs_cheongwon.service.ResponseService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.response.AnswerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseRepository responseRepository;
    private final ResponseService responseService;

    /**
     * 특정 답변 가져오기
     */
    @GetMapping("/{answer_id}")
    public ApiResponse<AnswerResponse> getResponseById(@PathVariable("answer_id") Long id) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_ANSWER_RETRIEVED, responseService.getResponseById(id));
    }

    /**
     * 해당 청원의 답변 가져오기
     */
    @GetMapping("/petition/{petition_id}")
    public ApiResponse<AnswerResponse> getResponsesByPetitionId(
            @PathVariable("petition_id") Long id) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_ANSWER_RETRIEVED, responseService.getResponsesByPetitionId(id));
    }
}
