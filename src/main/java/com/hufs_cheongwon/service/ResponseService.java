package com.hufs_cheongwon.service;

import com.hufs_cheongwon.domain.Admin;
import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.Response;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import com.hufs_cheongwon.repository.AdminRepository;
import com.hufs_cheongwon.repository.PetitionRepository;
import com.hufs_cheongwon.repository.ResponseRepository;
import com.hufs_cheongwon.web.dto.request.ResponseCreateRequest;
import com.hufs_cheongwon.web.dto.response.AnswerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AdminRepository adminRepository;
    private final PetitionRepository petitionRepository;

    public AnswerResponse getResponseById(Long id) {

        Response response = responseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 답변이 존재하지 않습니다. id= " + id));

        return AnswerResponse.createResponse(response.getId(), response.getContent(), response.getAdmin());
    }

    public List<AnswerResponse> getResponsesByPetitionId(Long petitionId) {
        List<Response> responses = responseRepository.findByPetitionId(petitionId);
        List<AnswerResponse> answerResponses = new ArrayList<>();
        for(Response response : responses) {
            answerResponses.add(AnswerResponse.createResponse(response.getId(), response.getContent(), response.getAdmin()));
        }
        return answerResponses;
    }


    @Transactional
    public AnswerResponse createResponse(ResponseCreateRequest request, Long adminId, Long petitionId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다. id: " + adminId));

        Petition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new IllegalArgumentException("청원을 찾을 수 없습니다. id: " + petitionId));

        Response response = Response.builder()
                .admin(admin)
                .petition(petition)
                .content(request.getContent())
                .build();

        petition.changePetitionStatus(PetitionStatus.ANSWER_COMPLETED);

        responseRepository.save(response);

        return AnswerResponse.createResponse(response.getId(), response.getContent(), response.getAdmin());
    }

    @Transactional
    public AnswerResponse deleteResponse(Long answerId) {
        Response response = responseRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변를 찾을 수 없습니다. id: " + answerId));
        responseRepository.deleteById(answerId);
        return AnswerResponse.createResponse(response.getId(), response.getContent(), response.getAdmin());
    }

    @Transactional
    public AnswerResponse updateResponse(Long answerId, ResponseCreateRequest request) {
        Response response = responseRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변를 찾을 수 없습니다. id: " + answerId));
        response.updateContent(request.getContent());
        responseRepository.save(response);
        return AnswerResponse.createResponse(response.getId(), response.getContent(), response.getAdmin());
    }
}
