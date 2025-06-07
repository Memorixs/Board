package com.board.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
@Slf4j
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody UserRequestDto request) {
		return userService.signup(request);
	}

	@PostMapping("/signin")
	public ResponseEntity<String> signin(@RequestBody UserRequestDto request, HttpServletResponse response,
		HttpSession session) {
		User user = null;
		try {
			user = userService.signin(request, session);
			Cookie cookie = new Cookie("id", user.getId().toString());
			response.addCookie(cookie);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST);
		}
		log.info("session name: {}", session.getAttribute(user.getId().toString()).toString());
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
}
