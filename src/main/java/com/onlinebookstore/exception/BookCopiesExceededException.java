package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class BookCopiesExceededException extends RuntimeException {
	public BookCopiesExceededException(final String bookName, final int copies, final int maxCopies) {
		super(String.format(ErrorMessages.NEW_BOOK_COPIES_EXCCEDED, bookName, copies, maxCopies));
	}
}
