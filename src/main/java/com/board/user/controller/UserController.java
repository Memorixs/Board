package com.board.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.board.user.dto.SignupDto;
import com.board.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	@PostMapping("/signup")
	public ResponseEntity<String> signup (@RequestBody SignupDto request) {
		return userService.signup(request);
	}
}
