package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookIdNotExistedException extends RuntimeException {
	public BookIdNotExistedException(final Long bookId) {
		super(String.format(ErrorMessages.BOOK_ID_NOT_EXISTED, bookId));
	}
}
