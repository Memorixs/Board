package com.board.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.board.user.dto.SignupDto;
import com.board.user.entity.User;
import com.board.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;

	public ResponseEntity<String> signup(SignupDto request) {
		User user = userRepository.findByEmail(request.getEmail());
		try {
			Assert.isNull(user, "이미 존재하는 회원입니다.");
		} catch(IllegalArgumentException e) {
			log.info("중복되는 이메일 입니다. email: {}", request.getEmail());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		user = userRepository.save(User.of(request));
		return new ResponseEntity<>(user.getId().toString(), HttpStatus.OK);
	}
}
