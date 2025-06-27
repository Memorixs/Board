package com.board.user.entity;

import java.io.Serializable;

import com.board.user.dto.UserRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50)
	private String email;
	@Column(length = 255)
	private String password;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public static User of(UserRequestDto dto) {
		return new User(dto.getEmail(), dto.getPassword());
	}
}
