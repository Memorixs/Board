package com.board.user.controller;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.board.dto.Response;
import com.board.user.dto.UserRequestDto;
import com.board.user.entity.User;
import com.board.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody UserRequestDto request) {
		try{
			userService.signup(request);
		} catch(RuntimeException e) {
			log.info(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);

	}

	@PostMapping("/signin")
	public ResponseEntity<String> signin(@RequestBody UserRequestDto request, HttpServletResponse response, HttpServletRequest req) {
		User user = null;
		//이미 로그인이 되어 있다면 기존 세션 재사용하면 된다. 로그인 하는 경우는 로그아웃 후 할 수 있으므로 매번 새로운 세션을 생성할 필요가 없다.
		// req.getSession(true);
		try {
			user = userService.signin(request, response, req);

		} catch (IllegalArgumentException e) {
			log.info(e.getMessage(), e);
			return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST);
		}
		log.info("session name: {}", req.getSession().getAttribute("user"));
		return new ResponseEntity<>(user.getId().toString(), HttpStatus.OK);
	}

	@GetMapping("/session")
	public void session(HttpServletRequest request, HttpSession session) {
		User user = null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals("id")) {
				user = (User) session.getAttribute(cookie.getValue());
			}
		}
		log.info("get User from session: {}", user.toString());
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		try {
			userService.logout(request, session, response);
		}catch (InternalException e) {
			log.info(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException e) {
			log.info(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok("로그아웃 성공");
	}

	@DeleteMapping("/users")
	public ResponseEntity<String> deletedAccount(HttpServletRequest request, HttpSession session, HttpServletResponse response)  {
		try{
			userService.delete(request, session, response);
		}catch (InternalException e) {
			log.info(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException e) {
			log.info(e.getMessage(), e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok("회원탈퇴 성공");
	}
}
