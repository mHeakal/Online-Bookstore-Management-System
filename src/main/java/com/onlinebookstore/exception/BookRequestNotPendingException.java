package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookRequestNotPendingException extends RuntimeException {
	public BookRequestNotPendingException(final Long bookRequestId) {
		super(String.format(ErrorMessages.BOOK_REQUEST_NOT_PENDING, bookRequestId));
	}
}
