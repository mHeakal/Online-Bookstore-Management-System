package com.onlinebookstore.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.onlinebookstore.constant.Constants;
import com.onlinebookstore.dto.BookBriefDto;
import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.dto.BookWithCategoryDto;
import com.onlinebookstore.dto.NewUserDto;
import com.onlinebookstore.entity.Book;
import com.onlinebookstore.entity.User;
import com.onlinebookstore.entity.UserBookRequest;
import com.onlinebookstore.entity.UserBookRequestStatus;
import com.onlinebookstore.entity.UserBrowsingHistory;
import com.onlinebookstore.exception.BookBorrowCopiesNotValidException;
import com.onlinebookstore.exception.BookIdNotExistedException;
import com.onlinebookstore.exception.BookNotAvailableException;
import com.onlinebookstore.exception.BookRequestInProgressException;
import com.onlinebookstore.exception.BookRequestNotApprovedException;
import com.onlinebookstore.exception.BookRequestNotCreatedException;
import com.onlinebookstore.exception.BookReturnedException;
import com.onlinebookstore.exception.UserAlreadyRegisteredException;
import com.onlinebookstore.exception.UserIdNotExistedException;
import com.onlinebookstore.mapper.BookMapper;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.repository.UserBookRequestRepository;
import com.onlinebookstore.repository.UserBrowsingHistoryRepository;
import com.onlinebookstore.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	private final UserRepository userRepository;
	private final UserBookRequestRepository userBookRequestRepository;
	private final UserBrowsingHistoryRepository userBrowsingHistoryRepository;

	public List<BookBriefDto> getAllBooks(final String category, final String name, final Long userId) {
		final var user = this.userRepository.findById(userId).orElseThrow(() -> new UserIdNotExistedException(userId));
		final List<Book> books;
		if (StringUtils.hasText(category) && StringUtils.hasText(name)) {
			books = this.bookRepository.findByNameContainsAndCategory(name, category);
		} else if (StringUtils.hasText(category)) {
			books = this.bookRepository.findByCategory(category);
		} else if (StringUtils.hasText(name)) {
			books = this.bookRepository.findByNameContains(name);
		} else {
			books = this.bookRepository.findAll();
		}
		adjustBrowsingNumber(books, user);
		return this.bookMapper.mapToBriefDto(books);
	}

	public List<BookWithCategoryDto> getAllBooksWithCategories(final String category, final Long userId) {
		final List<BookWithCategoryDto> bookWithCategoryDtos = new ArrayList<>();
		final var user = this.userRepository.findById(userId).orElseThrow(() -> new UserIdNotExistedException(userId));
		final List<Book> books;
		if (StringUtils.hasText(category)) {
			books = this.bookRepository.findByCategory(category);
		} else {
			books = this.bookRepository.findAll();
		}

		final Map<String, List<Book>> groupedBooksByCategory = books.stream()
				.collect(Collectors.groupingBy(Book::getCategory));

		for (final Map.Entry<String, List<Book>> entry : groupedBooksByCategory.entrySet()) {
			final String currentCategory = entry.getKey();
			final var currentCategoryBooks = this.bookMapper.mapToBriefDto(entry.getValue());
			final BookWithCategoryDto bookWithCategoryDto = BookWithCategoryDto.builder().category(currentCategory)
					.books(currentCategoryBooks).build();
			bookWithCategoryDtos.add(bookWithCategoryDto);
		}
		adjustBrowsingNumber(books, user);
		return bookWithCategoryDtos;
	}

	private void adjustBrowsingNumber(final List<Book> books, final User user) {
		for (final Book book : books) {
			adjustBrowsingHistory(user, book);
		}
		this.bookRepository.saveAll(books);
	}

	private void adjustBrowsingHistory(final User user, final Book book) {
		final UserBrowsingHistory userBrowsingHistory = getUserBrowsingHistory(user, book);
		userBrowsingHistory.setBrowsingHistory(userBrowsingHistory.getBrowsingHistory() + 1);
		this.userBrowsingHistoryRepository.save(userBrowsingHistory);
	}

	private UserBrowsingHistory getUserBrowsingHistory(final User user, final Book book) {
		final UserBrowsingHistory userBrowsingHistory;
		final var userBrowsingHistoryOptional = this.userBrowsingHistoryRepository
				.findByReferredUser_IdAndBook_Id(user.getId(), book.getId());
		userBrowsingHistory = userBrowsingHistoryOptional.orElseGet(
				() -> UserBrowsingHistory.builder().book(book).referredUser(user).browsingHistory(0L).build());
		return userBrowsingHistory;
	}

	public BookDto getBookDetailsById(final Long userId, final Long bookId) {
		final var user = this.userRepository.findById(userId).orElseThrow(() -> new UserIdNotExistedException(userId));
		final Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotExistedException(bookId));
		adjustBrowsingHistory(user, book);
		return this.bookMapper.mapToDto(book);
	}

	public String requestBorrow(final Long userId, final Long bookId) {
		final Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotExistedException(bookId));
		final var request = this.userBookRequestRepository.findByBook_IdAndReferredUser_IdAndStatus(bookId, userId,
				UserBookRequestStatus.PENDING);
		if (request.isPresent()) {
			throw new BookRequestInProgressException(book.getName());
		}
		final User user = this.userRepository.findById(userId).orElseThrow(() -> new UserIdNotExistedException(userId)); // TODO:
																															// handle
																															// exception
		if (book.getInStock() < 1) {
			throw new BookNotAvailableException(book.getName());
		}
		final UserBookRequest userBookRequest = UserBookRequest.builder().referredUser(user).book(book)
				.status(UserBookRequestStatus.PENDING).build();
		this.userBookRequestRepository.save(userBookRequest);
		return String.format(Constants.USER_REQUEST_RECEIVED, book.getName(), book.getNumberOfDaysForBorrow());
	}

	public String registerUser(final NewUserDto newUserDto) {
		final var existingUser = this.userRepository.findByNameAndPassword(newUserDto.getName(),
				newUserDto.getPassword());
		if (existingUser.isPresent()) {
			throw new UserAlreadyRegisteredException(newUserDto.getName());
		} else {
			this.userRepository
					.save(User.builder().name(newUserDto.getName()).password(newUserDto.getPassword()).build());
			return Constants.USER_ADDED_SUCCESSFULLY;
		}
	}

	public String returnBook(final Long userId, final Long bookId) {
		final var request = this.userBookRequestRepository.findByBook_IdAndReferredUser_Id(bookId, userId)
				.orElseThrow(() -> new BookRequestNotCreatedException(bookId, userId));
		if (!request.getStatus().equals(UserBookRequestStatus.APPROVED)) {
			throw new BookRequestNotApprovedException(request.getBook().getName());
		}
		if (request.getReturnedAt() != null) {
			throw new BookReturnedException(request.getBook().getName());
		}
		request.setReturnedAt(Timestamp.valueOf(LocalDateTime.now()));
		this.userBookRequestRepository.save(request);
		final Book book = request.getBook();
		book.setInStock(book.getInStock() + 1);
		if (book.getBorrowedCopiesCount() == 0) {
			throw new BookBorrowCopiesNotValidException(bookId);
		}
		book.setBorrowedCopiesCount(book.getBorrowedCopiesCount() - 1);
		this.bookRepository.save(book);
		if (ChronoUnit.DAYS.between(request.getUpdatedAt().toLocalDateTime(), LocalDateTime.now()) > request.getBook()
				.getNumberOfDaysForBorrow()) {
			return Constants.USER_BOOK_RETURNED_LATE;
		} else {
			return Constants.USER_BOOK_RETURNED;
		}
	}

	public List<BookBriefDto> getSuggestedBooks(final Long userId) {
		final List<Book> books = this.bookRepository.getSuggestedBooks(userId);
		return this.bookMapper.mapToBriefDto(books);
	}

	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}
}
