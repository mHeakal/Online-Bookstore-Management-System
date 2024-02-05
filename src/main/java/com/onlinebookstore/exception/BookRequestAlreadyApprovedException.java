package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookRequestAlreadyApprovedException extends RuntimeException {
	public BookRequestAlreadyApprovedException(final Long bookRequestId) {
		super(String.format(ErrorMessages.BOOK_REQUEST_ALREADY_APPROVED, bookRequestId));
	}
}
