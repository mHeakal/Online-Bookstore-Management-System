package com.onlinebookstore.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		final Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {

			final String fieldName = ((FieldError) error).getField();
			final String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final Throwable mostSpecificCause = ex.getMostSpecificCause();
		final Map<String, String> errors = new HashMap<>();
		if (mostSpecificCause instanceof UnrecognizedPropertyException) {
			final UnrecognizedPropertyException cause = (UnrecognizedPropertyException) mostSpecificCause;
			final String propertyName = cause.getPropertyName();
			final String message = "This property is not expected in the request body.";
			errors.put(propertyName, message);
			return new ResponseEntity(errors, headers, status);
		} else {
			final String exceptionName = "Error in the request";
			final String message = mostSpecificCause.getMessage();
			errors.put(exceptionName, message);
			return new ResponseEntity(exceptionName, headers, status);
		}
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookNameExistedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookNameExistedException(final BookNameExistedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookIdNotExistedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookIdNotExistedException(final BookIdNotExistedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookNameNotContainException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookNameNotContainException(final BookNameNotContainException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyRegisteredException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String userAlreadyRegisteredException(final UserAlreadyRegisteredException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookNotAvailableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookNotAvailableException(final BookNotAvailableException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookRequestInProgressException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookRequestInProgressException(final BookRequestInProgressException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookRequestNotApprovedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookRequestNotApprovedException(final BookRequestNotApprovedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookReturnedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookReturnedException(final BookReturnedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(UserIdNotExistedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String userIdNotExistedException(final UserIdNotExistedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookRequestNotCreatedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookRequestNotCreatedException(final BookRequestNotCreatedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookRequestAlreadyRejectedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookRequestAlreadyRejectedException(final BookRequestAlreadyRejectedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookRequestAlreadyApprovedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookRequestAlreadyApprovedException(final BookRequestAlreadyApprovedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookNameExistedInOtherBookException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookNameExistedInOtherBookException(final BookNameExistedInOtherBookException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookCopiesExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookCopiesExceededException(final BookCopiesExceededException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookIdDeleteNotAllowedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookIdDeleteNotAllowedException(final BookIdDeleteNotAllowedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookBorrowCopiesNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookBorrowCopiesNotValidException(final BookBorrowCopiesNotValidException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookBorrowedCannotDeleteException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookBorrowedCannotDeleteException(final BookBorrowedCannotDeleteException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@org.springframework.web.bind.annotation.ExceptionHandler(BookRequestNotPendingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookRequestNotPendingException(final BookRequestNotPendingException ex) {
		return ex.getMessage();
	}

}
