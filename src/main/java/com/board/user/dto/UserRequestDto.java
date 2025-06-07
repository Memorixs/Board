package com.board.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRequestDto {
	private String email;
	private String password;
}
