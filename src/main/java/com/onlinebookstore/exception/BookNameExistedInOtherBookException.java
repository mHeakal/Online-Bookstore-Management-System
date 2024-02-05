package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookNameExistedInOtherBookException extends RuntimeException {
	public BookNameExistedInOtherBookException(final String bookName) {
		super(String.format(ErrorMessages.BOOK_NAME_EXISTED_IN_ANOTHER_BOOK, bookName));
	}
}
