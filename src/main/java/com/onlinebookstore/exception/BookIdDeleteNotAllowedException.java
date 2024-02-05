package com.onlinebookstore.exception;

import static com.onlinebookstore.constant.ErrorMessages.BOOK_ID_DELETE_NOT_ALLOWED;

public class BookIdDeleteNotAllowedException extends RuntimeException {
	public BookIdDeleteNotAllowedException(final Long bookId) {
		super(String.format(BOOK_ID_DELETE_NOT_ALLOWED, bookId));
	}
}
