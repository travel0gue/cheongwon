package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.domain.Response;
import com.hufs_cheongwon.repository.ResponseRepository;
import com.hufs_cheongwon.service.ResponseService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<Response> getResponseById(@PathVariable("answer_id") Long id) {
        return ApiResponse.onSuccess(SuccessStatus.PETITION_ANSWER_RETRIEVED, responseService.getResponseById(id));
    }

    /**
     * 해당 청원의 답변 가져오기
     */
    @GetMapping("/{petition_id}")
    public ApiResponse<Page<Response>> getResponsesByPetitionId(
            @PathVariable("petition_id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(SuccessStatus.PETITION_ANSWER_RETRIEVED, responseRepository.findByPetitionId(id, pageable));
    }
}
