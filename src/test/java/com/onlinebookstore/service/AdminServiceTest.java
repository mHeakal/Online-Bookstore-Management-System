package com.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.onlinebookstore.TestUtil;
import com.onlinebookstore.constant.Constants;
import com.onlinebookstore.constant.ErrorMessages;
import com.onlinebookstore.dto.BookDto;
import com.onlinebookstore.entity.Book;
import com.onlinebookstore.entity.User;
import com.onlinebookstore.entity.UserBookRequest;
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

@ExtendWith({ MockitoExtension.class })
public class AdminServiceTest extends TestUtil {

	@Mock
	private BookRepository bookRepository;
	@Mock
	private BookMapper bookMapper;
	@Mock
	private UserService userService;
	@Mock
	private UserMapper userMapper;
	@Mock
	private UserBookRequestRepository userBookRequestRepository;
	@InjectMocks
	private AdminService adminService;

	@Test
	public void getAllBooks() {
		final List<Book> testBooks = getTestBooks();
		when(this.bookRepository.findAll()).thenReturn(testBooks);
		when(this.bookMapper.mapToDto(testBooks)).then(invocationOnMock -> BookMapper.INSTANCE.mapToDto(testBooks));
		final var result = this.adminService.getAllBooks();
		assertEquals(testBooks.size(), result.size());
	}

	@Test
	public void addBook() {
		final BookDto bookDto = getTestBookDto(1L, true);
		when(this.bookRepository.existsByName(bookDto.getName())).thenReturn(false);
		when(this.bookMapper.map(bookDto)).then(invocationOnMock -> BookMapper.INSTANCE.map(bookDto));

		ReflectionTestUtils.setField(this.adminService, "maxNumberOfBookCopies", 1);
		final var result = this.adminService.addBook(bookDto);
		Assertions.assertEquals(Constants.ADDED_SUCCESSFULLY, result);
	}

	@Test
	public void addBook_numberOfCopiesExceeded() {
		final BookDto bookDto = getTestBookDto(2L, true);
		when(this.bookRepository.existsByName(bookDto.getName())).thenReturn(false);
		ReflectionTestUtils.setField(this.adminService, "maxNumberOfBookCopies", 1);
		final BookCopiesExceededException exception = assertThrows(BookCopiesExceededException.class,
				() -> this.adminService.addBook(bookDto));
		assertEquals(String.format(ErrorMessages.NEW_BOOK_COPIES_EXCCEDED, bookDto.getName(), bookDto.getInStock(), 1),
				exception.getMessage());
	}

	@Test
	public void addBook_bookExisted() {
		final BookDto bookDto = getTestBookDto(2L, false);
		when(this.bookRepository.existsByName(bookDto.getName())).thenReturn(true);
		final BookNameExistedException exception = assertThrows(BookNameExistedException.class,
				() -> this.adminService.addBook(bookDto));
		assertEquals(String.format(ErrorMessages.BOOK_NAME_EXISTED, bookDto.getName()), exception.getMessage());
	}

	@Test
	public void updateBook() {
		final Long bookId = 3L;
		final BookDto bookDto = getTestBookDto(bookId);
		final Book testBook = getTestBook(bookId);
		when(this.bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
		when(this.bookRepository.existsByNameAndIdNotIn(bookDto.getName(), List.of(bookId))).thenReturn(false);
		this.adminService.updateBook(bookId, bookDto);
		verify(this.bookRepository, times(1)).save(testBook);
	}

	@Test
	public void updateBook_newNameAlreadyExisted() {
		final Long bookId = 3L;
		final BookDto bookDto = getTestBookDto(bookId);
		final Book testBook = getTestBook(bookId);
		when(this.bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
		when(this.bookRepository.existsByNameAndIdNotIn(bookDto.getName(), List.of(bookId))).thenReturn(true);
		final BookNameExistedInOtherBookException exception = assertThrows(BookNameExistedInOtherBookException.class,
				() -> this.adminService.updateBook(bookId, bookDto));
		assertEquals(String.format(ErrorMessages.BOOK_NAME_EXISTED_IN_ANOTHER_BOOK, bookDto.getName()),
				exception.getMessage());
	}

	@Test
	public void updateBook_bookNotFound() {
		final Long bookId = 3L;
		final BookDto bookDto = getTestBookDto(bookId);
		when(this.bookRepository.findById(bookId)).thenReturn(Optional.empty());
		final BookIdNotExistedException exception = assertThrows(BookIdNotExistedException.class,
				() -> this.adminService.updateBook(bookId, bookDto));
		assertEquals(String.format(ErrorMessages.BOOK_ID_NOT_EXISTED, bookId), exception.getMessage());
	}

	@Test
	public void getAllUsers() {
		final List<User> testUsers = getTestUsers();
		when(this.userService.getAllUsers()).thenReturn(testUsers);
		when(this.userMapper.mapToDto(testUsers)).then(invocationOnMock -> UserMapper.INSTANCE.mapToDto(testUsers));
		final var result = this.adminService.getAllUsers();
		assertEquals(testUsers.size(), result.size());
	}

	@Test
	public void getAllUserBookRequests() {
		final List<UserBookRequest> testUserBookRequests = getTestUserBookRequests();
		when(this.userBookRequestRepository.findAll()).thenReturn(testUserBookRequests);
		when(this.userMapper.mapToDto(any(User.class))).then(invocationOnMock -> {
			final User user = invocationOnMock.getArgument(0);
			return UserMapper.INSTANCE.mapToDto(user);
		});
		when(this.bookMapper.mapToDto(any(Book.class))).then(invocationOnMock -> {
			final Book book = invocationOnMock.getArgument(0);
			return BookMapper.INSTANCE.mapToDto(book);
		});
		final var result = this.adminService.getAllUserBookRequests();
		assertEquals(testUserBookRequests.size(), result.size());
	}

	@Test
	public void approve() {
		final Long id = 3L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.PENDING);
		testUserBookRequest.getBook().setInStock(1);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.of(testUserBookRequest));
		final var result = this.adminService.approve(id);
		verify(this.userBookRequestRepository, times(1)).save(testUserBookRequest);
		verify(this.bookRepository, times(1)).save(testUserBookRequest.getBook());
		assertEquals(String.format(Constants.USER_REQUEST_APPROVED, testUserBookRequest.getBook().getName()), result);
		assertFalse(testUserBookRequest.getBook().getInStock() > 0);
		assertEquals(UserBookRequestStatus.APPROVED, testUserBookRequest.getStatus());
	}

	@Test
	public void approve_bookNotAvailable() {
		final Long id = 3L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.PENDING);
		testUserBookRequest.getBook().setInStock(0);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.of(testUserBookRequest));
		final BookNotAvailableException exception = assertThrows(BookNotAvailableException.class,
				() -> this.adminService.approve(id));
		assertEquals(String.format(ErrorMessages.BOOK_NOT_AVAILABLE, testUserBookRequest.getBook().getName()),
				exception.getMessage());
	}

	@Test
	public void approve_requestNotCreated() {
		final Long id = 4L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.PENDING);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.empty());
		final BookRequestNotCreatedException exception = assertThrows(BookRequestNotCreatedException.class,
				() -> this.adminService.approve(id));
		assertEquals(String.format(ErrorMessages.BOOK_REQUEST_NOT_CREATED, id), exception.getMessage());
	}

	@Test
	public void approve_alreadyApproved() {
		final Long id = 2L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.APPROVED);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.of(testUserBookRequest));
		final BookRequestAlreadyApprovedException exception = assertThrows(BookRequestAlreadyApprovedException.class,
				() -> this.adminService.approve(id));
		assertEquals(String.format(ErrorMessages.BOOK_REQUEST_ALREADY_APPROVED, id), exception.getMessage());
	}

	@Test
	public void approve_notPending() {
		final Long id = 2L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.REJECTED);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.of(testUserBookRequest));
		final BookRequestNotPendingException exception = assertThrows(BookRequestNotPendingException.class,
				() -> this.adminService.approve(id));
		assertEquals(String.format(ErrorMessages.BOOK_REQUEST_NOT_PENDING, id), exception.getMessage());
	}

	@Test
	public void reject() {
		final Long id = 3L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.PENDING);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.of(testUserBookRequest));
		final var result = this.adminService.reject(id);
		verify(this.userBookRequestRepository, times(1)).save(testUserBookRequest);
		assertEquals(String.format(Constants.USER_REQUEST_REJECTED, testUserBookRequest.getBook().getName()), result);
		assertEquals(UserBookRequestStatus.REJECTED, testUserBookRequest.getStatus());
	}

	@Test
	public void reject_requestNotCreated() {
		final Long id = 3L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.PENDING);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.empty());
		final BookRequestNotCreatedException exception = assertThrows(BookRequestNotCreatedException.class,
				() -> this.adminService.reject(id));
		assertEquals(String.format(ErrorMessages.BOOK_REQUEST_NOT_CREATED, id), exception.getMessage());
	}

	@Test
	public void reject_alreadyRejected() {
		final Long id = 8L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.REJECTED);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.of(testUserBookRequest));
		final BookRequestAlreadyRejectedException exception = assertThrows(BookRequestAlreadyRejectedException.class,
				() -> this.adminService.reject(id));
		assertEquals(String.format(ErrorMessages.BOOK_REQUEST_ALREADY_REJECTED, id), exception.getMessage());
	}

	@Test
	public void reject_notPending() {
		final Long id = 8L;
		final UserBookRequest testUserBookRequest = getTestUserBookRequest(id, UserBookRequestStatus.APPROVED);
		when(this.userBookRequestRepository.findById(id)).thenReturn(Optional.of(testUserBookRequest));
		final BookRequestNotPendingException exception = assertThrows(BookRequestNotPendingException.class,
				() -> this.adminService.reject(id));
		assertEquals(String.format(ErrorMessages.BOOK_REQUEST_NOT_PENDING, id), exception.getMessage());
	}

	@Test
	public void deleteBook() {
		final Long bookId = 4L;
		final Book testBook = getTestBook(bookId);
		when(this.bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
		final List<UserBookRequest> testUserBookRequests = getTestUserBookRequests();
		testUserBookRequests.forEach(x -> x.setStatus(UserBookRequestStatus.APPROVED));
		testUserBookRequests.forEach(x -> x.setReturnedAt(Timestamp.valueOf(LocalDateTime.now())));
		when(this.userBookRequestRepository.findByBook_Id(bookId)).thenReturn(testUserBookRequests);
		final var result = this.adminService.deleteBook(bookId);
		verify(this.userBookRequestRepository, times(1)).deleteAll(anyList());
		verify(this.bookRepository, times(1)).delete(testBook);
		assertEquals(String.format(Constants.BOOK_DELETED_SUCCESSFULLY, bookId), result);
	}

	@Test
	public void deleteBook_bookNotFound() {
		final Long bookId = 4L;
		final Book testBook = getTestBook(bookId);
		when(this.bookRepository.findById(bookId)).thenReturn(Optional.empty());
		final BookIdNotExistedException exception = assertThrows(BookIdNotExistedException.class,
				() -> this.adminService.deleteBook(bookId));
		assertEquals(String.format(ErrorMessages.BOOK_ID_NOT_EXISTED, bookId), exception.getMessage());
	}

	@Test
	public void deleteBook_pendingRequests() {
		final Long bookId = 4L;
		final Book testBook = getTestBook(bookId);
		when(this.bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
		final List<UserBookRequest> testUserBookRequests = getTestUserBookRequests();
		when(this.userBookRequestRepository.findByBook_Id(bookId)).thenReturn(testUserBookRequests);
		final BookIdDeleteNotAllowedException exception = assertThrows(BookIdDeleteNotAllowedException.class,
				() -> this.adminService.deleteBook(bookId));
		assertEquals(String.format(ErrorMessages.BOOK_ID_DELETE_NOT_ALLOWED, bookId), exception.getMessage());
	}

	@Test
	public void deleteBook_notReturnedBooks() {
		final Long bookId = 4L;
		final Book testBook = getTestBook(bookId);
		when(this.bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
		final List<UserBookRequest> testUserBookRequests = getTestUserBookRequests();
		testUserBookRequests.forEach(x -> x.setStatus(UserBookRequestStatus.APPROVED));
		when(this.userBookRequestRepository.findByBook_Id(bookId)).thenReturn(testUserBookRequests);
		final BookBorrowedCannotDeleteException exception = assertThrows(BookBorrowedCannotDeleteException.class,
				() -> this.adminService.deleteBook(bookId));
		assertEquals(String.format(ErrorMessages.BOOK_ID_BORROWED_DELETE_NOT_ALLOWED, bookId), exception.getMessage());
	}
}
