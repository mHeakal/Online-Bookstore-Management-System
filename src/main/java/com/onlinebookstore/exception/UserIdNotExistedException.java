package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class UserIdNotExistedException extends RuntimeException {
	public UserIdNotExistedException(final Long userId) {
		super(String.format(ErrorMessages.USER_ID_NOT_EXISTED, userId));
	}
}
