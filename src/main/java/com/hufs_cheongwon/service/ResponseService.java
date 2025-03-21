package com.hufs_cheongwon.service;

import com.hufs_cheongwon.common.exception.InvalidStateException;
import com.hufs_cheongwon.common.exception.ResourceNotFoundException;
import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.Response;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import com.hufs_cheongwon.repository.AdminRepository;
import com.hufs_cheongwon.repository.PetitionRepository;
import com.hufs_cheongwon.repository.ResponseRepository;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import com.hufs_cheongwon.web.dto.request.ResponseCreateRequest;
import com.hufs_cheongwon.web.dto.response.AnswerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AdminRepository adminRepository;
    private final PetitionRepository petitionRepository;

    public AnswerResponse getResponseById(Long id) {

        Response response = responseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorStatus.PETITION_NOT_FOUND));

        return AnswerResponse.from(response, response.getAdmin());
    }

    public AnswerResponse getResponsesByPetitionId(Long petitionId) {
        Response response = responseRepository.findOptionalByPetitionId(petitionId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorStatus.ANSWER_NOT_FOUND));

        return AnswerResponse.from(response, response.getAdmin());
    }


    @Transactional
    public AnswerResponse createResponse(ResponseCreateRequest request, Long adminId, Long petitionId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorStatus.ADMIN_NOT_FOUND));

        Petition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorStatus.PETITION_NOT_FOUND));

        if(responseRepository.findByPetitionId(petitionId) != null){
            throw new InvalidStateException(ErrorStatus.ALREADY_ANSWERED);
        }

        Response response = Response.builder()
                .admin(admin)
                .petition(petition)
                .content(request.getContent())
                .build();

        petition.changePetitionStatus(PetitionStatus.ANSWER_COMPLETED);

        responseRepository.save(response);

        return AnswerResponse.from(response, response.getAdmin());
    }

    @Transactional
    public AnswerResponse deleteResponse(Long answerId) {
        Response response = responseRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorStatus.ANSWER_NOT_FOUND));
        responseRepository.deleteById(answerId);
        return AnswerResponse.from(response, response.getAdmin());
    }

    @Transactional
    public AnswerResponse updateResponse(Long answerId, ResponseCreateRequest request) {
        Response response = responseRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorStatus.ANSWER_NOT_FOUND));
        response.updateContent(request.getContent());
        responseRepository.save(response);
        return AnswerResponse.from(response, response.getAdmin());
    }
}
