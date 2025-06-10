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
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
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
			.map(Board::entityToDto).toList();
	}

	public static BoardDto entityToDto(Board board) {
		String email = getEmailFromBoard(board);
		return new BoardDto(board.getId(), board.getTitle(), board.getContent(), board.getCreatedAt(),
			board.getUpdatedAt(), email);
	}

	private static String getEmailFromBoard(Board board) {
		return board.getUser() == null ? "알수없음" : board.getUser().getEmail();
	}

	public void update(CreateBoardDto dto) {
		String content = dto.getContent();
		String title = dto.getTitle();

		this.content = !content.isBlank() ? content : this.content;
		this.title = !title.isBlank() ? title : this.title;
		this.updatedAt = LocalDate.now();
	}

	static public void deleteUser(List<Board> boards) {
		boards.forEach(board -> board.setUser(null));
	}

}
