package com.hufs_cheongwon.web.controller;

import com.hufs_cheongwon.common.exception.InvalidStateException;
import com.hufs_cheongwon.common.exception.ResourceNotFoundException;
import com.hufs_cheongwon.domain.Board;
import com.hufs_cheongwon.domain.enums.BoardType;
import com.hufs_cheongwon.repository.BoardRepository;
import com.hufs_cheongwon.service.BoardService;
import com.hufs_cheongwon.web.apiResponse.ApiResponse;
import com.hufs_cheongwon.web.apiResponse.error.ErrorStatus;
import com.hufs_cheongwon.web.apiResponse.success.SuccessStatus;
import com.hufs_cheongwon.web.dto.request.BoardCreateRequest;
import com.hufs_cheongwon.web.dto.request.BoardUpdateRequest;
import com.hufs_cheongwon.web.dto.response.BoardResponse;
import jakarta.validation.Valid;
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
    private final BoardService boardService;
    private static final String ACCESS_KEY = "cheong34!";

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

    /**
     * 게시글 생성
     */
    @PostMapping("/{type}")
    public ApiResponse<BoardResponse> createBoard(
            @PathVariable(name = "type") BoardType type,
            @RequestBody @Valid BoardCreateRequest request
    ) {
        checkKey(request.getKey());

        // 요청에서 받은 type과 request의 boardType이 일치하는지 검증
        if (request.getBoardType() != type) {
            throw new InvalidStateException(ErrorStatus.INVALID_BOARD_TYPE);
        }

        Board createdBoard = boardService.createBoard(request);
        return ApiResponse.onSuccess(BoardResponse.from(createdBoard, type));
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{type}/{board_id}")
    public ApiResponse<BoardResponse> updateBoard(
            @PathVariable(name = "type") BoardType type,
            @PathVariable(name = "board_id") Long id,
            @RequestBody @Valid BoardUpdateRequest request
    ) {
        checkKey(request.getKey());

        // 요청에서 받은 type과 request의 boardType이 일치하는지 검증
        if (request.getBoardType() != type) {
            throw new InvalidStateException(ErrorStatus.INVALID_BOARD_TYPE);
        }

        Board updatedBoard = boardService.updateBoard(id, request);

        // 수정된 게시글의 타입 확인
        if (updatedBoard.getBoardType() != type) {
            throw new InvalidStateException(ErrorStatus.INVALID_BOARD_TYPE);
        }

        return ApiResponse.onSuccess(BoardResponse.from(updatedBoard, type));
    }

    private void checkKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new InvalidStateException(ErrorStatus.KEY_WRONG);
        }
        if (key.equals(ACCESS_KEY)) {
            return;
        }
        throw new InvalidStateException(ErrorStatus.KEY_WRONG);
    }
}

