package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookRequestAlreadyRejectedException extends RuntimeException {
	public BookRequestAlreadyRejectedException(final Long bookRequestId) {
		super(String.format(ErrorMessages.BOOK_REQUEST_ALREADY_REJECTED, bookRequestId));
	}
}
