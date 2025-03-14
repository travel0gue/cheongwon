package com.hufs_cheongwon.web.dto.response;

import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.enums.Category;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "청원 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PetitionResponse {

    private String title;
    private Category category;
    private String content;
    private PetitionStatus petitionStatus;
    private Integer agreeCount;
    private Integer viewCount;
    private Integer reportCount;
    private String writerEmail;

    public static PetitionResponse from(Petition petition) {
        return PetitionResponse.builder()
                .title(petition.getTitle())
                .category(petition.getCategory())
                .content(petition.getContent())
                .petitionStatus(petition.getPetitionStatus())
                .agreeCount(petition.getAgreeCount())
                .viewCount(petition.getViewCount())
                .reportCount(petition.getReportCount())
                .writerEmail(petition.getUsers().getEmail())
                .build();
    }
}
