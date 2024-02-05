package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookRequestInProgressException extends RuntimeException {
	public BookRequestInProgressException(final String bookName) {
		super(String.format(ErrorMessages.BOOK_REQUEST_IN_PROGRESS, bookName));
	}
}
