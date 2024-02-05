package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookRequestNotCreatedException extends RuntimeException {
	public BookRequestNotCreatedException(final Long bookRequestId) {
		super(String.format(ErrorMessages.BOOK_REQUEST_NOT_CREATED, bookRequestId));
	}

	public BookRequestNotCreatedException(final Long bookId, final Long userId) {
		super(String.format(ErrorMessages.BOOK_REQUEST_WITH_BOOK_USER_NOT_CREATED, bookId, userId));
	}
}
