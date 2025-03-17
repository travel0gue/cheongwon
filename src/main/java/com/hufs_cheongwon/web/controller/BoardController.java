package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.common.exception.InvalidStateException;
import com.hufs_cheongwon.common.exception.ResourceNotFoundException;
import com.hufs_cheongwon.domain.Board;
import com.hufs_cheongwon.domain.enums.BoardType;
import com.hufs_cheongwon.repository.BoardRepository;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorStatus.BOARD_NOT_FOUND));

        // 요청한 boardType과 실제 데이터의 boardType이 일치하는지 검증
        if (board.getBoardType() != type) {
            throw new InvalidStateException(ErrorStatus.INVALID_BOARD_TYPE);
        }
        return ApiResponse.onSuccess(SuccessStatus.BOARD_RETRIEVED, BoardResponse.from(board, type));
    }
}
