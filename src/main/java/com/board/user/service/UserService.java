package com.board.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.board.board.entity.Board;
import com.board.board.repository.BoardRepository;
import com.board.board.service.BoardService;
import com.board.user.dto.UserRequestDto;
import com.board.user.entity.User;
import com.board.user.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;

	public Long signup(UserRequestDto request) {
		User user = userRepository.findByEmail(request.getEmail());
		try {
			Assert.isNull(user, "이미 존재하는 회원입니다.");
		} catch(IllegalArgumentException e) {
			log.info("중복되는 이메일 입니다. email: {}", request.getEmail());
			throw new RuntimeException("이미 존재하는 회원입니다.");
		}
		user = userRepository.save(User.of(request));
		return user.getId();
	}

	public User signin(UserRequestDto request, HttpSession session, HttpServletResponse response) {
		User user = userRepository.findByEmail(request.getEmail());
		try {
			Assert.notNull(user, "존재하지 않는 회원입니다.");
			session.setAttribute(user.getId().toString(), user);
			Cookie cookie = createCookie(user.getId());
			response.addCookie(cookie);
		} catch(IllegalArgumentException e) {
			log.info("존재하지 않는 회원입니다. email: {}", request.getEmail());
			throw new IllegalArgumentException(e);
		}
		return user;

	}

	@Transactional
	public void logout(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		deletedSession(request, session);
		deleteCookie(response);
	}

	@Transactional
	public void delete(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		Long id = deletedSession(request, session);
		deleteCookie(response);
		//유저 삭제전에 게시판에 연관된 유저값을 null로
		List<Board> boards = boardRepository.findByUserId(id);
		Board.deleteUser(boards); //update -> delete 하이버네이트 디폴트값임
		userRepository.deleteById(id);
	}

	private Long deletedSession(HttpServletRequest request, HttpSession session) {
		String id = BoardService.getUserIdFromCookie(request);
		session.removeAttribute(id);
		return Long.parseLong(id);
	}

	private void deleteCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("id", "");
		cookie.setPath("/api");
		cookie.setMaxAge(0); // 즉시 만료
		response.addCookie(cookie);
	}

	private Cookie createCookie(Long id) {
		Cookie cookie = new Cookie("id", id.toString());
		cookie.setPath("/api");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60 * 60 * 24 * 7); //1주일 초단위
		return cookie;
	}
}
