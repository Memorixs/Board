package com.board.board.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.board.dto.BoardDto;
import com.board.board.dto.CreateBoardDto;
import com.board.board.dto.ListType;
import com.board.board.entity.Board;
import com.board.board.repository.BoardRepository;
import com.board.user.entity.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
	private final BoardRepository boardRepository;

	@Transactional
	public Long save(CreateBoardDto requestDto, HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("user");

		Board board = boardRepository.save(Board.from(requestDto, user));
		return board.getId();
	}

	private User getUserById(HttpSession session, String id) {
		User user = (User) session.getAttribute("user"); //새로운 요청이여도 세션이 삭제되지 않았음. 쿠키의 SESSIONID으로 DB에서 조회
		Optional.ofNullable(user)
			.orElseThrow(() -> new InternalException("서버 내부에서 에러가 발생하였습니다."));
		return user;
	}

	public static String getSessionIdFromCookie(HttpServletRequest request) {
		String id = null;
		for (Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals("SESSION")) {
				id = cookie.getValue();
				break;
			}
		}
		if (id == null) {
			throw new RuntimeException("쿠키에 사용자 세션이 없습니다. 다시 로그인해주세요.");
		}
		return id;
	}

	public ListType<BoardDto> getAll() {
		List<Board> boards = boardRepository.findAll();
		List<BoardDto> response = Board.entityToDto(boards);
		return new ListType<BoardDto>(response.size(), response);
	}

	public BoardDto getById(Long id) {
		Board board = boardRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("게시판이 존재하지 않습니다."));
		return Board.entityToDto(board);
	}

	@Transactional
	public BoardDto updateById(Long id, CreateBoardDto requestDto, HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Board board = findById(id);

		if (!checkAuthorization(user.getId(), board)) {
			throw new RuntimeException("수정할 권한이 없습니다.");
		}

		board.update(requestDto);
		return new BoardDto(board.getId(), board.getTitle(), board.getContent(), board.getCreatedAt(),
			board.getUpdatedAt(), board.getUser().getEmail());
	}

	private boolean checkAuthorization(Long userId, Board board) {
		Optional.ofNullable(board.getUser()).orElseThrow(() -> new NullPointerException("탈퇴한 사용자의 게시글 입니다. 삭제할 권한이 없습니다."));
		return userId.equals(board.getUser().getId());
	}

	private Board findById(Long id) {
		return boardRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("게시판이 존재하지 않습니다."));
	}

	@Transactional
	public void deleteById(Long id, HttpServletRequest request, HttpSession session) {
		String sessionId = getSessionIdFromCookie(request);
		//delete session from db

		User user = (User) session.getAttribute("user");
		Board board = findById(id);
		if (!checkAuthorization(user.getId(), board)) {
			throw new RuntimeException("삭제할 권한이 없습니다.");
		}
		boardRepository.deleteById(id);
	}
}
