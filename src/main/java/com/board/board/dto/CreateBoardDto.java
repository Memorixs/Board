package com.board.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateBoardDto {
	private String title;
	private String content;
}
