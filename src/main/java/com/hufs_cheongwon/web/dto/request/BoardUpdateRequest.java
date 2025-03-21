package com.hufs_cheongwon.web.dto.request;

import com.hufs_cheongwon.domain.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {
    private String key;
    private String title;
    private String content;
    private BoardType boardType;
}
