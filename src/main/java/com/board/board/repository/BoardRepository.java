package com.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
