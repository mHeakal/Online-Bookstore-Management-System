package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookNameExistedException extends RuntimeException {
	public BookNameExistedException(final String bookName) {
		super(String.format(ErrorMessages.BOOK_NAME_EXISTED, bookName));
	}
}
