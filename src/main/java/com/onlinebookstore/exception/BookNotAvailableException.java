package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookNotAvailableException extends RuntimeException {
	public BookNotAvailableException(final String bookName) {
		super(String.format(ErrorMessages.BOOK_NOT_AVAILABLE, bookName));
	}
}
