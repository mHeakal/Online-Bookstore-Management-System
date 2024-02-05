package com.onlinebookstore.exception;

import com.onlinebookstore.constant.ErrorMessages;

public class UserAlreadyRegisteredException extends RuntimeException {
	public UserAlreadyRegisteredException(final String userName) {
		super(String.format(ErrorMessages.USER_ALREADY_REGISTERED, userName));
	}
}
