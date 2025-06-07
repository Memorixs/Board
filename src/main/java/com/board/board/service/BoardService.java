package com.board.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.board.dto.BoardDto;
import com.board.board.dto.CreateBoardDto;
import com.board.board.dto.ListType;
import com.board.board.entity.Board;
import com.board.board.repository.BoardRepository;
import com.board.user.entity.User;
import com.board.user.repository.UserRepository;

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
	private final UserRepository userRepository;

	@Transactional
	public Long save(CreateBoardDto requestDto, HttpSession session, HttpServletRequest request) {
		String userId = getUserIdFromCookie(request);
		User user = getUserById(session, userId);

		Optional.ofNullable(user)
			.orElseThrow(() -> new RuntimeException("해당 id 세션이 존재하지 않습니다. 세션 생성을 위해 다시 로그인해주세요. id: " + userId));

		Board board = boardRepository.save(Board.from(requestDto, user));
		return board.getId();
	}

	private User getUserById(HttpSession session, String id) {
		return (User) session.getAttribute(id);
	}

	private String getUserIdFromCookie(HttpServletRequest request) {
		String id = null;
		for (Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals("id")) {
				id = cookie.getValue();
				break;
			}
		}
		return id;
	}

	public ListType<BoardDto> getAll() {
		List<Board> boards = boardRepository.findAll();
		List<BoardDto> response = Board.entityToDto(boards);
		return new ListType<BoardDto>(response.size(), response);
	}
}
