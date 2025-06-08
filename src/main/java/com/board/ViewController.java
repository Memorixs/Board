package com.board;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.board.board.dto.BoardDto;
import com.board.board.dto.ListType;
import com.board.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ViewController {
	private final BoardService boardService;
	@GetMapping
	public ModelAndView main() {
		ListType<BoardDto> response = boardService.getAll();
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Object> result = new HashMap<>();
		result.put("total", response.getTotal());
		result.put("boards", response.getResponse());
		modelAndView.addAllObjects(result);
		modelAndView.setViewName("index");
		return modelAndView;
	}
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}

	@GetMapping("/write")
	public String writeFrom() {
		return "write";
	}

	@GetMapping("/board/all")
	public ModelAndView getAll() {
		ListType<BoardDto> response = boardService.getAll();
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Object> result = new HashMap<>();
		result.put("boards", response.getResponse());
		result.put("total", response.getTotal());
		// return new ResponseEntity<>(response, HttpStatus.OK);
		return new ModelAndView("boardList", result);
	}

	@GetMapping("/board/{id}")
	public ModelAndView getById(@PathVariable Long id) {
		BoardDto response = null;
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("boardDetails");
		try {
			response = boardService.getById(id);
		} catch (RuntimeException e) {
			// return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			modelAndView.setStatus(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
			return modelAndView;
		}
		modelAndView.addObject("board", response);
		// return new ResponseEntity<>(response, HttpStatus.OK);
		return modelAndView;
	}

}
