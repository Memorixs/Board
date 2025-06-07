package com.board.user.entity;

import com.board.user.dto.SignupDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50)
	private String email;
	@Column(length = 255)
	private String password;

	private User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public static User of(SignupDto dto) {
		return new User(dto.getEmail(), dto.getPassword());
	}
}
