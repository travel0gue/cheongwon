package com.hufs_cheongwon.web.dto.response;

import com.hufs_cheongwon.common.Constant;
import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.enums.Category;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.*;

@Schema(description = "청원 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PetitionResponse {

    private Long id;
    private String title;
    private Category category;
    private String content;
    private PetitionStatus petitionStatus;
    private Integer agreeCount;
    private Integer viewCount;
    private Integer reportCount;
    private String writerEmail;
    private LocalDate createDate;
    private LocalDate endDate;

    public static PetitionResponse from(Petition petition) {
        return PetitionResponse.builder()
                .id(petition.getId())
                .title(petition.getTitle())
                .category(petition.getCategory())
                .content(petition.getContent())
                .petitionStatus(petition.getPetitionStatus())
                .agreeCount(petition.getAgreeCount())
                .viewCount(petition.getViewCount())
                .reportCount(petition.getReportCount())
                .writerEmail(petition.getUsers().getEmail())
                .createDate(petition.getCreatedAt().toLocalDate())
                .endDate(petition.getCreatedAt().toLocalDate().plus(Constant.PETITION_ACTIVE_PERIOD))
                .build();
    }
}
