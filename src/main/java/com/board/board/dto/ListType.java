package com.board.board.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListType<T extends  Response> {
	private int total;
	private List<T> response;
}
