package com.hufs_cheongwon.web.dto.response;

import com.hufs_cheongwon.domain.Board;
import com.hufs_cheongwon.domain.enums.BoardType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoardType boardType;

    public static BoardResponse from(Board board, BoardType boardType) {
        return BoardResponse.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .boardType(boardType)
                .build();
    }
}
