package com.onlinebookstore.exception;

import static com.onlinebookstore.constant.ErrorMessages.BOOK_ID_NUMBER_OF_BORROWED_COPIES_LESS_ZERO;

public class BookBorrowCopiesNotValidException extends RuntimeException {
	public BookBorrowCopiesNotValidException(final Long bookId) {
		super(String.format(BOOK_ID_NUMBER_OF_BORROWED_COPIES_LESS_ZERO, bookId));
	}
}
