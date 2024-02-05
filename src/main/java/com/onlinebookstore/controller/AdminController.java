package com.onlinebookstore.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.UserBookRequestDto;
import com.onlinebookstore.dto.UserDto;
import com.onlinebookstore.service.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/admins")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "APIs for managing books by admins")
public class AdminController {
	private final AdminService adminService;

	@GetMapping("/books")
	public List<BookDto> getAllBooks() {
		return this.adminService.getAllBooks();
	}

	@PostMapping("/books")
	ResponseEntity<String> addBook(@Valid @RequestBody final BookDto bookDto) {
		return new ResponseEntity<>(this.adminService.addBook(bookDto), HttpStatus.CREATED);
	}

	@DeleteMapping("/books/{id}")
	ResponseEntity<String> deleteBook(@PathVariable final Long id) {
		return new ResponseEntity<>(this.adminService.deleteBook(id), HttpStatus.OK);
	}

	@PutMapping("/books/{bookId}")
	ResponseEntity<String> updateBook(@PathVariable final Long bookId, @Valid @RequestBody final BookDto bookDto) {
		return new ResponseEntity<>(this.adminService.updateBook(bookId, bookDto), HttpStatus.OK);
	}

	@GetMapping("/users")
	public List<UserDto> getAllUsers() {
		return this.adminService.getAllUsers();
	}

	@GetMapping("/requests")
	public List<UserBookRequestDto> getAllUserBookRequests() {
		return this.adminService.getAllUserBookRequests();
	}

	@PutMapping("/requests/{id}/approve")
	ResponseEntity<String> approve(@PathVariable final Long id) {
		return new ResponseEntity<>(this.adminService.approve(id), HttpStatus.OK);
	}

	@PutMapping("/requests/{id}/reject")
	ResponseEntity<String> reject(@PathVariable final Long id) {
		return new ResponseEntity<>(this.adminService.reject(id), HttpStatus.OK);
	}
}
