package com.onlinebookstore.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.BookBriefDto;
import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.BookWithCategoryDto;
import com.onlinebookstore.dto.NewUserDto;
import com.onlinebookstore.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("{userId}/books")
	public List<BookBriefDto> getAllBooks(@RequestParam(name = "category", required = false) final String category,
			@RequestParam(name = "name", required = false) final String name,
			@PathVariable("userId") final Long userId) {
		return this.userService.getAllBooks(category, name, userId);
	}

	@GetMapping("{userId}/books/categories")
	public List<BookWithCategoryDto> getAllBooksWithCategories(
			@RequestParam(name = "category", required = false) final String category,
			@PathVariable("userId") final Long userId) {
		return this.userService.getAllBooksWithCategories(category, userId);
	}

	@GetMapping("{id}/books/suggest")
	public List<BookBriefDto> suggestBooks(@PathVariable("id") final Long userId) {
		return this.userService.getSuggestedBooks(userId);
	}

	@GetMapping("{userId}/books/{bookId}")
	public BookDto getBookDetailsById(@PathVariable("userId") final Long userId,
			@PathVariable("bookId") final Long bookId) {
		return this.userService.getBookDetailsById(userId, bookId);
	}

	@PostMapping("")
	ResponseEntity<String> register(@Valid @RequestBody final NewUserDto newUserDto) {
		return new ResponseEntity<>(this.userService.registerUser(newUserDto), HttpStatus.CREATED);
	}

	@PostMapping("{userId}/books/{bookId}/borrow")
	ResponseEntity<String> request(@PathVariable final Long userId, @PathVariable final Long bookId) {
		return new ResponseEntity<>(this.userService.requestBorrow(userId, bookId), HttpStatus.OK);
	}

	@PutMapping("{userId}/books/{bookId}/return")
	ResponseEntity<String> returnBook(@PathVariable final Long userId, @PathVariable final Long bookId) {
		return new ResponseEntity<>(this.userService.returnBook(userId, bookId), HttpStatus.OK);
	}
}
