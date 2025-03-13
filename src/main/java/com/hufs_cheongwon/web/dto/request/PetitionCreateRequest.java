package com.hufs_cheongwon.web.dto.request;

import com.hufs_cheongwon.domain.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetitionCreateRequest {

    @NotBlank(message = "청원 제목은 필수입니다.")
    @Size(min = 5, max = 100, message = "제목은 5자 이상 100자 이하여야 합니다.")
    private String title;

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @NotBlank(message = "청원 내용은 필수입니다.")
    @Size(min = 20, message = "내용은 20자 이상이어야 합니다.")
    private String content;
}
