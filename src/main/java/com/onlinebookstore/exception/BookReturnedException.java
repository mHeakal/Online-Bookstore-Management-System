package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookReturnedException extends RuntimeException {
	public BookReturnedException(final String bookName) {
		super(String.format(ErrorMessages.BOOK_RETURNED, bookName));
	}
}
