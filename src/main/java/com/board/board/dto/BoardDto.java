package com.board.board.dto;

import java.time.LocalDate;

import com.board.user.dto.UserRequestDto;
import com.board.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto implements Response{
	private Long id;
	private String title;
	private String content;
	//snake case로 반환하고 싶은데 전에 사용하던게 deprecated... 방법을 못찾음
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private String email;
}
