package com.board.board.entity;

import java.time.LocalDate;
import java.util.List;

import com.board.board.dto.BoardDto;
import com.board.board.dto.CreateBoardDto;
import com.board.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 10)
	private String title;
	private String content;
	private LocalDate createdAt;
	private LocalDate updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private Board(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.createdAt = LocalDate.now();
		this.updatedAt = LocalDate.now();
		this.user = user;
	}

	public static Board from(CreateBoardDto dto, User user) {
		return new Board(dto.getTitle(), dto.getContent(), user);
	}

	public static List<BoardDto> entityToDto(List<Board> boards) {
		return boards.stream()
			.map(board ->
				new BoardDto(board.getId(), board.getTitle(), board.getContent(), board.getCreatedAt(), board.getUpdatedAt()))
			.toList();
	}


}
