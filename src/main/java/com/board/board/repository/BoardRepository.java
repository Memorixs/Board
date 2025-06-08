package com.board.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findByUserId(Long userId);
}
