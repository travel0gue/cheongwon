package com.hufs_cheongwon.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCreateRequest {

    @NotBlank(message = "답변 내용은 필수입니다.")
    @Size(min = 20, message = "내용은 20자 이상이어야 합니다.")
    private String content;
}
