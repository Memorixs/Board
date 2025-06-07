package com.board.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.board.dto.CreateBoardDto;
import com.board.board.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
	private final BoardService boardService;

	@PostMapping
	public ResponseEntity<String> create( HttpServletRequest request, @RequestBody CreateBoardDto requestDto, HttpSession session){
		Long id;
		try{
			id = boardService.save(requestDto, session, request);
		} catch(RuntimeException e) {
			log.info(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
	}
}
