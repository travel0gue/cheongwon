package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.domain.Board;
import com.hufs_cheongwon.domain.enums.BoardType;
import com.hufs_cheongwon.repository.BoardRepository;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;

    /**
     * 공지 사항 목록 조회
     */
    @GetMapping("/{type}")
    public ApiResponse<Page<BoardResponse>> getNotices(
            @PathVariable(name = "type")BoardType type,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardPage = boardRepository.findByBoardType(type, pageable);

        Page<BoardResponse> responsePage = boardPage.map(board -> BoardResponse.from(board, type));

        return ApiResponse.onSuccess(SuccessStatus.BOARDS_RETRIEVED, responsePage);
    }

    /**
     * 특정 공지 조회
     */
    @GetMapping("/{type}/{board_id}")
    public ApiResponse<BoardResponse> getBoardById(
            @PathVariable(name = "type")BoardType type,
            @PathVariable(name = "board_id")Long id
    ) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new );

    }

    /**
     * QnA 목록 조회
     */
    @GetMapping("/qna")
    public ApiResponse<Page<BoardResponse>> getQnAs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){

    }

    /**
     * 특정 QnA 조회
     */
    @GetMapping("/qna/{qna_id}")
    public ApiResponse<BoardResponse> getQnAById(@PathVariable(name = "notice_id")Long id)
}
