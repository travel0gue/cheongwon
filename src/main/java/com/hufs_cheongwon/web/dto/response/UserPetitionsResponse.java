package com.hufs_cheongwon.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class UserPetitionsResponse {
    private Page<PetitionResponse> writtenPetitions;  // 사용자가 작성한 청원
    private Page<PetitionResponse> agreedPetitions;   // 사용자가 동의한 청원
}
