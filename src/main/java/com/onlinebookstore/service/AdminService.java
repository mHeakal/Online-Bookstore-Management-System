package com.onlinebookstore.service;

import static com.onlinebookstore.constant.Constants.ADDED_SUCCESSFULLY;
import static com.onlinebookstore.constant.Constants.BOOK_DELETED_SUCCESSFULLY;
import static com.onlinebookstore.constant.Constants.UPDATED_SUCCESSFULLY;
import static com.onlinebookstore.constant.Constants.USER_REQUEST_APPROVED;
import static com.onlinebookstore.constant.Constants.USER_REQUEST_REJECTED;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.UserBookRequestDto;
import com.onlinebookstore.dto.UserDto;
import com.onlinebookstore.entity.Book;
import com.onlinebookstore.entity.User;
import com.onlinebookstore.entity.UserBookRequestStatus;
import com.onlinebookstore.exception.BookBorrowedCannotDeleteException;
import com.onlinebookstore.exception.BookCopiesExceededException;
import com.onlinebookstore.exception.BookIdDeleteNotAllowedException;
import com.onlinebookstore.exception.BookIdNotExistedException;
import com.onlinebookstore.exception.BookNameExistedException;
import com.onlinebookstore.exception.BookNameExistedInOtherBookException;
import com.onlinebookstore.exception.BookNotAvailableException;
import com.onlinebookstore.exception.BookRequestAlreadyApprovedException;
import com.onlinebookstore.exception.BookRequestAlreadyRejectedException;
import com.onlinebookstore.exception.BookRequestNotCreatedException;
import com.onlinebookstore.exception.BookRequestNotPendingException;
import com.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.mapper.UserMapper;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.repository.UserBookRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	private final UserService userService;
	private final UserMapper userMapper;
	private final UserBookRequestRepository userBookRequestRepository;

	@Value("${max.number.of.book.copies}")
	private Integer maxNumberOfBookCopies;

	public List<BookDto> getAllBooks() {
		final List<Book> books = this.bookRepository.findAll();
		return this.bookMapper.mapToDto(books);
	}

	public String addBook(final BookDto bookDto) {
		validate(bookDto);
		this.bookRepository.save(this.bookMapper.map(bookDto));
		return ADDED_SUCCESSFULLY;
	}

	private void validate(final BookDto bookDto) {
		if (this.bookRepository.existsByName(bookDto.getName())) {
			throw new BookNameExistedException(bookDto.getName());
		}
		if (bookDto.getInStock() > this.maxNumberOfBookCopies) {
			throw new BookCopiesExceededException(bookDto.getName(), bookDto.getInStock(), this.maxNumberOfBookCopies);
		}
	}

	public String updateBook(final Long bookId, final BookDto bookDto) {
		final Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotExistedException(bookId));
		final String newBookName = bookDto.getName();
		if (this.bookRepository.existsByNameAndIdNotIn(newBookName, List.of(bookId))) {
			throw new BookNameExistedInOtherBookException(newBookName);
		}
		updateBook(bookDto, book);
		this.bookRepository.save(book);
		return UPDATED_SUCCESSFULLY;
	}

	private void updateBook(final BookDto bookDto, final Book book) {
		if (bookDto.getName() != null) {
			book.setName(bookDto.getName());
		}
		if (bookDto.getAuthorName() != null) {
			book.setAuthorName(bookDto.getAuthorName());
		}
		if (bookDto.getPrice() != null) {
			book.setPrice(bookDto.getPrice());
		}
		if (bookDto.getInStock() != null) {
			book.setInStock(bookDto.getInStock());
		}
		if (bookDto.getCategory() != null) {
			book.setCategory(bookDto.getCategory());
		}
		if (bookDto.getNumberOfDaysForBorrow() != null) {
			book.setNumberOfDaysForBorrow(bookDto.getNumberOfDaysForBorrow());
		}
		if (bookDto.getBorrowedCopiesCount() != null) {
			book.setBorrowedCopiesCount(bookDto.getBorrowedCopiesCount());
		}
		if (bookDto.getStockLevel() != null) {
			book.setStockLevel(bookDto.getStockLevel());
		}
	}

	public List<UserDto> getAllUsers() {
		final List<User> users = this.userService.getAllUsers();
		return this.userMapper.mapToDto(users);
	}

	public List<UserBookRequestDto> getAllUserBookRequests() {
		final var requests = this.userBookRequestRepository.findAll();
		final List<UserBookRequestDto> allUserBookRequestDtos = new ArrayList<>();
		for (final var request : requests) {
			final UserDto userDto = this.userMapper.mapToDto(request.getReferredUser());
			final BookDto bookDto = this.bookMapper.mapToDto(request.getBook());
			final LocalDateTime updatedAt = request.getUpdatedAt() == null ? null
					: request.getUpdatedAt().toLocalDateTime();
			final LocalDateTime returnedAt = request.getReturnedAt() == null ? null
					: request.getReturnedAt().toLocalDateTime();
			final UserBookRequestDto userBookRequestDto = UserBookRequestDto.builder().userDto(userDto).bookDto(bookDto)
					.status(request.getStatus()).requestedAt(request.getRequestedAt().toLocalDateTime())
					.id(request.getId()).updatedAt(updatedAt).returnedAt(returnedAt).build();
			allUserBookRequestDtos.add(userBookRequestDto);
		}
		return allUserBookRequestDtos;
	}

	public String approve(final Long requestId) {
		final var request = this.userBookRequestRepository.findById(requestId)
				.orElseThrow(() -> new BookRequestNotCreatedException(requestId));
		if (request.getStatus().equals(UserBookRequestStatus.APPROVED)) {
			throw new BookRequestAlreadyApprovedException(requestId);
		}
		if (!request.getStatus().equals(UserBookRequestStatus.PENDING)) {
			throw new BookRequestNotPendingException(requestId);
		}
		if (request.getBook().getInStock() < 1) {
			throw new BookNotAvailableException(request.getBook().getName());
		}
		request.setStatus(UserBookRequestStatus.APPROVED);
		request.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
		this.userBookRequestRepository.save(request);
		final Book book = request.getBook();
		book.setInStock(book.getInStock() - 1);
		book.setBorrowedCopiesCount(book.getBorrowedCopiesCount() + 1);
		this.bookRepository.save(book);
		return String.format(USER_REQUEST_APPROVED, book.getName());
	}

	public String reject(final Long requestId) {
		final var request = this.userBookRequestRepository.findById(requestId)
				.orElseThrow(() -> new BookRequestNotCreatedException(requestId));
		if (request.getStatus().equals(UserBookRequestStatus.REJECTED)) {
			throw new BookRequestAlreadyRejectedException(requestId);
		}
		if (!request.getStatus().equals(UserBookRequestStatus.PENDING)) {
			throw new BookRequestNotPendingException(requestId);
		}
		request.setStatus(UserBookRequestStatus.REJECTED);
		request.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
		this.userBookRequestRepository.save(request);
		return String.format(USER_REQUEST_REJECTED, request.getBook().getName());
	}

	public String deleteBook(final Long bookId) {
		final var book = this.bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotExistedException(bookId));
		final var requests = this.userBookRequestRepository.findByBook_Id(bookId);
		if (requests.stream().anyMatch(x -> x.getStatus().equals(UserBookRequestStatus.PENDING))) {
			throw new BookIdDeleteNotAllowedException(bookId);
		}
		if (requests.stream()
				.anyMatch(x -> x.getStatus().equals(UserBookRequestStatus.APPROVED) && x.getReturnedAt() == null)) {
			throw new BookBorrowedCannotDeleteException(bookId);
		}
		this.userBookRequestRepository.deleteAll(requests);
		this.bookRepository.delete(book);
		return String.format(BOOK_DELETED_SUCCESSFULLY, bookId);
	}
}
