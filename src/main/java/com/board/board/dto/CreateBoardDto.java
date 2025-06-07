package com.board.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class CreateBoardDto {
	private String title;
	private String content;
}
