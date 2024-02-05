package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookRequestNotApprovedException extends RuntimeException {
	public BookRequestNotApprovedException(final String bookName) {
		super(String.format(ErrorMessages.BOOK_REQUEST_NOT_APPROVED, bookName));
	}
}
