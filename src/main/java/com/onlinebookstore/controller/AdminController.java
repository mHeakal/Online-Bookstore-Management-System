package com.onlinebookstore.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.annotation.Version;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/admins")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "APIs for managing books by admins")
public class AdminController {
	private final AdminService adminService;

	@Version
	@GetMapping("/books")
	public List<BookDto> getAllBooks() {
		log.info("Get/books");
		return this.adminService.getAllBooks();
	}

	@Version
	@PostMapping("/books")
	ResponseEntity<String> addBook(@Valid @RequestBody final BookDto bookDto) {
		log.info("Post/books");
		return new ResponseEntity<>(this.adminService.addBook(bookDto), HttpStatus.CREATED);
	}

	@Version
	@DeleteMapping("/books/{id}")
	ResponseEntity<String> deleteBook(@PathVariable final Long id) {
		log.info("Delete/books");
		return new ResponseEntity<>(this.adminService.deleteBook(id), HttpStatus.OK);
	}

	@Version
	@PutMapping("/books/{bookId}")
	ResponseEntity<String> updateBook(@PathVariable final Long bookId, @Valid @RequestBody final BookDto bookDto) {
		log.info("Put/books");
		return new ResponseEntity<>(this.adminService.updateBook(bookId, bookDto), HttpStatus.OK);
	}

	@Version
	@GetMapping("/users")
	public List<UserDto> getAllUsers() {
		log.info("Get/users");
		return this.adminService.getAllUsers();
	}

	@Version
	@GetMapping("/requests")
	public List<UserBookRequestDto> getAllUserBookRequests() {
		log.info("Get/requests");
		return this.adminService.getAllUserBookRequests();
	}

	@Version
	@PutMapping("/requests/{id}/approve")
	ResponseEntity<String> approve(@PathVariable final Long id) {
		log.info("Put/requests/approve");
		return new ResponseEntity<>(this.adminService.approve(id), HttpStatus.OK);
	}

	@Version
	@PutMapping("/requests/{id}/reject")
	ResponseEntity<String> reject(@PathVariable final Long id) {
		log.info("Put/requests/reject");
		return new ResponseEntity<>(this.adminService.reject(id), HttpStatus.OK);
	}
}
