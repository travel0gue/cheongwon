package com.hufs_cheongwon.service;

import com.hufs_cheongwon.domain.Board;
import com.hufs_cheongwon.repository.BoardRepository;
import com.hufs_cheongwon.web.dto.request.BoardCreateRequest;
import com.hufs_cheongwon.web.dto.request.BoardUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시글 생성
    @Transactional
    public Board createBoard(BoardCreateRequest request) {
        Board board = Board.builder()
                .writer(request.getWriter())
                .title(request.getTitle())
                .content(request.getContent())
                .boardType(request.getBoardType())
                .build();

        return boardRepository.save(board);
    }

    // 게시글 수정
    @Transactional
    public Board updateBoard(Long id, BoardUpdateRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. ID: " + id));

        // 수정 권한 확인 로직 (필요시 추가)

        board.update(request.getTitle(), request.getContent(), request.getBoardType());
        return board;
    }
}
