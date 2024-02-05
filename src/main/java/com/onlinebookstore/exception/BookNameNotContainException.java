package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookNameNotContainException extends RuntimeException {
	public BookNameNotContainException(final String bookName) {
		super(String.format(ErrorMessages.BOOK_NAME_NOT_CONTAIN, bookName));
	}
}
