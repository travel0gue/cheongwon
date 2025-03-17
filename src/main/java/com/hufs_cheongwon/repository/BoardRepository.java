package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Board;
import com.hufs_cheongwon.domain.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByBoardType(BoardType boardType, Pageable pageable);
}
